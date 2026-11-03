package com.serviceconnect.service;
import com.serviceconnect.model.UserAccount; import com.serviceconnect.repo.UserAccountRepository; import jakarta.servlet.http.HttpSession; import org.springframework.stereotype.Component;
@Component public class CurrentUser {
 private final UserAccountRepository users; public CurrentUser(UserAccountRepository users){this.users=users;}
 public UserAccount require(HttpSession s){Object id=s.getAttribute("userId"); if(id==null) throw new IllegalArgumentException("Please log in first."); return users.findById((Long)id).orElseThrow(()->new IllegalArgumentException("Account not found."));}
 public UserAccount requireRole(HttpSession s,String role){UserAccount u=require(s); if(!u.role.equals(role)) throw new IllegalArgumentException("This action requires a "+role.toLowerCase()+" account."); return u;}
}
