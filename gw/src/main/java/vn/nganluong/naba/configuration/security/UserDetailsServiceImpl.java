package vn.nganluong.naba.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import vn.nganluong.naba.entities.WebUsers;
import vn.nganluong.naba.repository.WebUsersRepository;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private WebUsersRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException {
		WebUsers user = userRepository.findByUserNameAndStatus(username, true);
		
		if (user == null) {
			throw new UsernameNotFoundException("Could not find user");
		}
		
		return new MyUserDetails(user);
	}

}