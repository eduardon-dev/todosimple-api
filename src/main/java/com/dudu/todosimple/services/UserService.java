package com.dudu.todosimple.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dudu.todosimple.models.User;
import com.dudu.todosimple.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	
	// Procurar usuário pelo id
	public User findById(Long id) { 
		Optional<User> user = this.userRepository.findById(id); //É opicional eu receber de volta esse usuário , eu printo , caso contrário , print que não foi encontrado . 
		return user.orElseThrow(() -> new RuntimeException( 
				"Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()
				));
	}
	
	
	//Criar usuário
	@Transactional
	public User create(User obj) {
		obj.setId(null);
		obj = this.userRepository.save(obj);
		return obj;
	}
	
	
	//Atualizar senha
	@Transactional
	public User update(User obj) {
		User newObj = findById(obj.getId());
		newObj.setPassword(obj.getPassword());
		return this.userRepository.save(newObj);
	}
	
	
	//Deletar usuário
	public void delete(Long id) {
		findById(id);
		try {
			this.userRepository.deleteById(id);
		} catch ( Exception e ){
			throw new RuntimeException( 
					"Não é possível excluir pois há tarefas relacionadas!"
					);
		}
	}
	
}