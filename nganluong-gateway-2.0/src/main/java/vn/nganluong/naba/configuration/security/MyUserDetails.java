package vn.nganluong.naba.configuration.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vn.nganluong.naba.entities.WebUsers;
import vn.nganluong.naba.entities.WebUsersRoles;

public class MyUserDetails implements UserDetails {

	private static final long serialVersionUID = 9186874325090231536L;
	private WebUsers user;

	public MyUserDetails(WebUsers user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Set<WebUsersRoles> userRoles = user.getWebUsersRoleses();
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		for (WebUsersRoles userRole : userRoles) {
			authorities.add(new SimpleGrantedAuthority(userRole.getWebRoles().getName()));
		}

		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getStatus();
	}

}
