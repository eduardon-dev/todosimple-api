package com.dudu.todosimple.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dudu.todosimple.models.Task;
import com.dudu.todosimple.models.User;
import com.dudu.todosimple.repositories.TaskRepository;

@Service
public class TaskService{
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserService userService;
	
	
	public Task findById(Long id) { 
		Optional<Task> task = this.taskRepository.findById(id); //É opcional eu receber de volta esse usuário , eu printo , caso contrário , print que não foi encontrado . 
		return task.orElseThrow(() -> new RuntimeException(
				"Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
	}
	
	public List<Task> findAllByUserId(Long userId){ //busca todas as tasks de um usuário
		List<Task> tasks = taskRepository.findByUser_Id(userId);
		return tasks;
	}
	
	
	
	// User_id
	// Description
	// id
	@Transactional
	public Task create(Task obj) {
		User user = this.userService.findById(obj.getUser().getId()); // Pega o id do dono da task
		obj.setId(null); // seta o id da task como nulo
		obj.setUser(user); // seta o user que pegamos acima como o dono da task
		obj = this.taskRepository.save(obj); // salva a nova task no banco de dados
		return obj; // retorna a task como valor da função
	}
	
	@Transactional
	public Task update(Task obj) {
		Task newObj = findById(obj.getId()); //verifica se existe a task no banco
		newObj.setDescription(obj.getDescription()); // Pega a description lançada no valor da função e seta na Task que foi buscada
		return this.taskRepository.save(newObj); // Salva
	}
	
	public void delete(Long id) {
		findById(id);
		try {
			this.taskRepository.deleteById(id);
		} 
		catch ( Exception e ) {
			throw new RuntimeException ("Não é possível excluir pois há entidades relacionadas!");
		}
		
			
		
	}
}