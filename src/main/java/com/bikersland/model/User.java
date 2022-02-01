package com.bikersland.model;

import java.util.Date;

import com.bikersland.utility.ConvertMethods;

import javafx.scene.image.Image;

public class User {
	private Integer id;
	private String name;
	private String surname;
	private String username;
	private String email;
	private String password;
	private Image image;
	private Date createTime;
	
	public User() 
	{
		this(null, null, null, null, null, null, null, null);
	}
	
	public User(String name, String surname, String username, String email, String password, Image image) 
	{
		this(null, name, surname, username, email, password, image, null);
	}
	
	public User(Integer id, String name, String surname, String username, String email, String password, Image image, Date createTime) 
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

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getUsername() 
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}
	
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword() 
	{
		return password;
	}

	public void setPassword(String password) 
	{
		this.password = password;
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
