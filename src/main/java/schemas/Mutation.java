package schemas;

import com.coxautodev.graphql.tools.GraphQLRootResolver;

import authentication.AuthData;
import authentication.SigninPayload;
import graphql.GraphQLException;
import models.Update;
import models.User;
import utilities.JDBC;
import repositories.UserRepository;


public class Mutation implements GraphQLRootResolver {
	private final UserRepository userRepository;
	
	public Mutation(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User createUser(AuthData auth) {
		User newUser = new User(auth.getUsername(), auth.getPassword());
		userRepository.saveUser(newUser);
		return newUser;
	}
	
	public SigninPayload signinUser(AuthData auth) throws IllegalAccessException {
		User user = userRepository.findByUsername(auth.getUsername());
		if (user.getPassword().equals(auth.getPassword())) {
			return new SigninPayload(user.getId(), user);
		}
		throw new GraphQLException("Either username or password is incorrect");
 	}
	
	public User updateNotificationPrice(String id, double notifyPrice) {	
		userRepository.updateNotifyPriceById(Integer.parseInt(id), notifyPrice);
		User user = userRepository.findById(id);
		System.out.println(user.getNotifyPrice());
		return user;
	}
	
	public Update comparePrice(String id) {
		return new Update(userRepository.comparePrice(id));

	}

}
