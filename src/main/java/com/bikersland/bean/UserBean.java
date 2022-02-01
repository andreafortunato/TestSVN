package com.bikersland.bean;

import java.io.File;
import java.util.Date;

import org.apache.commons.validator.routines.EmailValidator;

import com.bikersland.exception.ImageFileException;
import com.bikersland.exception.user.EmailException;
import com.bikersland.exception.user.NameException;
import com.bikersland.exception.user.PasswordException;
import com.bikersland.exception.user.SurnameException;
import com.bikersland.exception.user.UsernameException;
import com.bikersland.utility.ConvertMethods;
import com.bikersland.Main;

import javafx.scene.image.Image;

public class UserBean
{
	private Integer id;
	private String name;
	private String surname;
	private String username;
	private String email;
	private String password;
	private Image image;
	private Date createTime;
	
	public UserBean() 
	{
		this(null, null, null, null, null, null, null, null);
	}
	
	public UserBean(String name, String surname, String username, String email, String password, Image image)
	{
		this(null, name, surname, username, email, password, image, null);
	}
	
	public UserBean(Integer id, String name, String surname, String username, String email, String password, Image image, Date createTime) 
	{
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.image = image;
		this.createTime = createTime;
	}
	
	public Integer getId() 
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) throws NameException 
	{
		String strippedName = name.strip();
		validateName(strippedName);
		this.name = strippedName;
	}
	
	private void validateName(String strippedName) throws NameException
	{		
		if(strippedName.length() < 2)
			throw new NameException(Main.getBundle().getString("ex_user_name_short"));
		
		if(strippedName.length() > 32)
			throw new NameException(Main.getBundle().getString("ex_user_name_long"));
	}

	public String getSurname() 
	{
		return surname;
	}

	public void setSurname(String surname) throws SurnameException 
	{
		String strippedSurname = surname.strip();
		validateSurname(strippedSurname);
		this.surname = strippedSurname;
	}
	
	private void validateSurname(String strippedSurname) throws SurnameException
	{
		if(strippedSurname.length() < 2)
			throw new SurnameException(Main.getBundle().getString("ex_user_surname_short"));
		
		if(strippedSurname.length() > 32)
			throw new SurnameException(Main.getBundle().getString("ex_user_surname_long"));
	}


	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username) throws UsernameException
	{
		String strippedUsername = username.strip();
		validateUsername(strippedUsername);
		this.username = strippedUsername;
	}
	
	private void validateUsername(String strippedUsername) throws UsernameException 
	{
		if(strippedUsername.length() < 2)
			throw new UsernameException(Main.getBundle().getString("ex_user_username_short"));
		
		if(strippedUsername.length() > 16)
			throw new UsernameException(Main.getBundle().getString("ex_user_username_long"));
	}

	public String getEmail() 
	{
		return email;
	}

	public void setEmail(String email) throws EmailException 
	{
		String strippedEmail = email.strip();
		validateEmail(strippedEmail);
		this.email = strippedEmail;
	}
	
	private void validateEmail(String strippedEmail) throws EmailException 
	{		
		if(strippedEmail.length() < 6 ||
				strippedEmail.length() > 128 ||
				!EmailValidator.getInstance().isValid(strippedEmail))
			throw new EmailException(Main.getBundle().getString("ex_invalid_email"));
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) throws PasswordException
	{
		validatePassword(password);
		this.password = password;
	}
	
	private void validatePassword(String password) throws PasswordException 
	{
		if(password.length() < 6)
			throw new PasswordException(Main.getBundle().getString("ex_user_password_short"));
		if(password.length() > 64)
			throw new PasswordException(Main.getBundle().getString("ex_user_password_long"));
		if(!password.matches(".*\\d.*"))
			throw new PasswordException(Main.getBundle().getString("ex_user_password_no_number"));
		if(!password.matches(".*[A-Z].*"))
			throw new PasswordException(Main.getBundle().getString("ex_user_password_no_capital_letter"));
		if(!password.matches(".*[.!@#$%&*()_+=|<>?{}\\[\\]~-].*"))
			throw new PasswordException(Main.getBundle().getString("ex_user_password_no_special_char"));
	}

	public Date getCreateTime() 
	{
		return createTime;
	}

	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}
	
	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image) 
	{
		this.image = image;
	}
	
	public void setImage(File imageFile) throws ImageFileException
	{
		validateImageFile(imageFile);
		setImage(new Image(imageFile.toURI().toString()));
	}
	
	private void validateImageFile(File imageFile) throws ImageFileException 
	{
    	if(imageFile.length() > 4194304)
    		throw new ImageFileException(Main.getBundle().getString("ex_image_too_big"));
	}

	public String toString()
	{
		String userString = "ID: " + id + "\nName: " + this.name + 
        		"\nSurname: " + this.surname + "\nUsername: " + this.username +
        		"\nEmail: " + this.email + "\nPassword: " + this.password +
        		"\nCreate_time: ";
		
		if(this.createTime == null)
			userString += this.createTime;
		else
			userString += ConvertMethods.dateToLocalFormat(this.createTime);
		
		return userString;
	}
}
