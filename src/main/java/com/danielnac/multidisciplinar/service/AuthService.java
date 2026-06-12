package com.danielnac.multidisciplinar.service;

import com.danielnac.multidisciplinar.dto.RegistroRequest;
import com.danielnac.multidisciplinar.exception.BadRequestException;
import com.danielnac.multidisciplinar.exception.ConflictException;
import com.danielnac.multidisciplinar.exception.ForbiddenException;
import com.danielnac.multidisciplinar.exception.UnauthorizedException;
import com.danielnac.multidisciplinar.model.Usuario;
import com.danielnac.multidisciplinar.repository.UsuarioRepository;
import com.danielnac.multidisciplinar.security.PasswordHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario autenticar(String email, String senha) {
        validarEmail(email);

        Usuario usuario = usuarioRepository.obterPorEmail(email);

        validarUsuario(usuario, senha);

        return usuario;
    }

    public void registrar(RegistroRequest request) {
        validarEmail(request.email());

        if (usuarioRepository.obterPorEmail(request.email()) != null) {
            throw new ConflictException("E-mail já está em uso.", "CONFLICT");
        }

        if (request.nome() == null || request.nome().isBlank()) {
            throw new BadRequestException("O campo 'nome' é obrigatório.");
        }

        if (request.cargo() == null) {
            throw new BadRequestException("O campo 'cargo' é obrigatório.");
        }

        validarSenhaForte(request.senha());

        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(PasswordHelper.encrypt(request.senha()));
        usuario.setCargo(request.cargo());
        usuario.setUnidadeId(request.unidadeId());
        usuario.setAtivo(true);

        usuarioRepository.incluir(usuario);
    }

    public void alterarSenha(Integer usuarioId, String senhaAtual, String senhaNova, String senhaNovaValidate) {
        Usuario usuario = usuarioRepository.obterPorId(usuarioId);
        if (usuario == null) {
            throw new BadRequestException("Usuário não encontrado.");
        }

        validarNovaSenha(senhaAtual, senhaNova, senhaNovaValidate);

        if (!PasswordHelper.validate(senhaAtual, usuario.getSenha())) {
            throw new UnauthorizedException("Senha atual inválida.");
        }

        usuarioRepository.atualizarSenha(usuarioId, PasswordHelper.encrypt(senhaNova));
    }

    private void validarEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException("O campo 'email' é obrigatório.");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new BadRequestException("O campo 'email' deve ser um endereço de e-mail válido.");
        }
    }

    private void validarNovaSenha(String senhaAtual, String senhaNova, String senhaNovaValidate) {
        if (senhaAtual == null || senhaAtual.isBlank() || senhaNova == null || senhaNova.isBlank()) {
            throw new BadRequestException("O campo de 'senha' é obrigatório.");
        }
        if (!senhaNova.equals(senhaNovaValidate)) {
            throw new BadRequestException("A senha nova e a confirmação não coincidem.");
        }
        validarSenhaForte(senhaNova);
    }

    private void validarSenhaForte(String senha) {
        if (senha == null || senha.isBlank()) {
            throw new BadRequestException("O campo 'senha' é obrigatório.");
        }
        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$")) {
            throw new BadRequestException("Senha não corresponde aos critérios de segurança (mín. 8 chars, maiúscula, minúscula e número).");
        }
    }

    private void validarUsuario(Usuario usuario, String senha) {
        if (usuario == null || !PasswordHelper.validate(senha, usuario.getSenha())) {
            throw new UnauthorizedException("E-mail ou senha inválidos.");
        }
        if (!usuario.getAtivo()) {
            throw new ForbiddenException("Usuário inativo.");
        }
    }
}
