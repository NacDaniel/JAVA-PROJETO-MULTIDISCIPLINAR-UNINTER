package com.danielnac.multidisciplinar.controller;

import com.danielnac.multidisciplinar.dto.RegistroRequest;
import com.danielnac.multidisciplinar.dto.TokenDTO;
import com.danielnac.multidisciplinar.model.Usuario;
import com.danielnac.multidisciplinar.security.JWTHelper;
import com.danielnac.multidisciplinar.service.AuthService;
import com.danielnac.multidisciplinar.support.SessionUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
public class authController {

    @Value("${cookie.secure:false}")
    private boolean cookieSecure;

    @Autowired
    private AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<Void> registro(@RequestBody RegistroRequest request) {
        authService.registrar(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestHeader("email") String email, @RequestHeader("senha") String senha, HttpServletResponse response) {

        Usuario usuario = authService.autenticar(email, senha);
        TokenDTO tokenDTO = JWTHelper.createJwtToken(usuario);

        ResponseCookie cookie = ResponseCookie.from("token", tokenDTO.getToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSecure ? "None" : "Lax")
                .path("/")
                .maxAge(Duration.ofHours(2))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        tokenDTO.setToken(null);

        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSecure ? "None" : "Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/alterar-senha")
    public ResponseEntity<Void> alterarSenha(@RequestHeader("senhaAtual") String senhaAtual, @RequestHeader("senhaNova") String senhaNova,
                                             @RequestHeader("senhaNovaValidate") String senhaNovaValidate) {
        authService.alterarSenha(SessionUtil.getId(), senhaAtual, senhaNova, senhaNovaValidate);
        return ResponseEntity.noContent().build();
    }
}
