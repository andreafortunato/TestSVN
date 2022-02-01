package com.bikersland.bean;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.exception.ImageFileException;
import com.bikersland.exception.event.TitleException;
import com.bikersland.utility.ConvertMethods;
import com.bikersland.Main;

import javafx.scene.image.Image;

public class EventBean 
{
	private Integer id;
	private String title;
	private String description;
	private String ownerUsername;
	private String departureCity;
	private String destinationCity;
	private Date departureDate;
	private Date returnDate;
	private Image image;
	private Date createTime;
	private List<String> tags = new ArrayList<>();
	
	public EventBean() 
	{
		this(null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public EventBean(String title, String description, String ownerUsername, String departureCity, String destinationCity, Date departureDate, Date returnDate, Image image, List<String> tags) 
	{
		this(null, title, description, ownerUsername, departureCity, destinationCity, departureDate, returnDate, image, null, tags);
	}
	
	public EventBean(Integer id, String title, String description, String ownerUsername, String departureCity, String destinationCity, Date departureDate, Date returnDate, Image image, Date createTime, List<String> tags) 
	{
		this.id = id;
		this.title = title;
		this.description = description;
		this.ownerUsername = ownerUsername;
		this.departureCity = departureCity;
		this.destinationCity = destinationCity;
		this.departureDate = departureDate;
		this.returnDate = returnDate;
		this.image = image;
		this.createTime = createTime;
		this.tags = tags;
	}
	
	public String toString() 
	{
		return String.format("ID: %s\n"
				+ "Title: %s\n"
				+ "Description: %s\n"
				+ "Owner Username: %s\n"
				+ "Departure City: %s\n"
				+ "Destination City: %s\n"
				+ "Departure Date: %s\n"
				+ "Return Date: %s\n"
				+ "Tag List: %s\n"
				+ "Create time: %s",
				id, title, description, ownerUsername, departureCity,
				destinationCity, ConvertMethods.dateToLocalFormat(departureDate),
				ConvertMethods.dateToLocalFormat(returnDate), tags.toString(),
				ConvertMethods.dateToLocalFormat(createTime));
	}

	public Integer getId() 
	{
		return id;
	}

	public void setId(Integer id) 
	{
		this.id = id;
	}

	public String getTitle() 
	{
		return title;
	}

	public void setTitle(String title) throws TitleException 
	{
		String strippedTitle = title.strip();
		validateTitle(strippedTitle);
		this.title = strippedTitle;
	}
	
	private void validateTitle(String strippedTitle) throws TitleException 
	{
		if(strippedTitle.length() < 4)
			throw new TitleException(Main.getBundle().getString("ex_event_title_short"));
		
		if(strippedTitle.length() > 64)
			throw new TitleException(Main.getBundle().getString("ex_event_title_long"));
	}

	public String getDescription() 
	{
		return description;
	}

	public void setDescription(String description) 
	{
		this.description = description;
	}

	public String getOwnerUsername() 
	{
		return ownerUsername;
	}

	public void setOwnerUsername(String ownerUsername) 
	{
		this.ownerUsername = ownerUsername;
	}

	public String getDepartureCity() 
	{
		return departureCity;
	}

	public void setDepartureCity(String departureCity) 
	{
		this.departureCity = departureCity;
	}

	public String getDestinationCity() 
	{
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) 
	{
		this.destinationCity = destinationCity;
	}

	public Date getDepartureDate() 
	{
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) 
	{
		this.departureDate = departureDate;
	}
	public void setDepartureDate(String departureDate) 
	{
		this.departureDate = Date.valueOf(departureDate);
	}

	public Date getReturnDate() 
	{
		return returnDate;
	}

	public void setReturnDate(Date returnDate) 
	{
		this.returnDate = returnDate;
	}
	
	public void setReturnDate(String returnDate) 
	{
		this.returnDate = Date.valueOf(returnDate);
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
	
	public Date getCreateTime() 
	{
		return createTime;
	}

	public void setCreateTime(Date createTime) 
	{
		this.createTime = createTime;
	}

	public List<String> getTags() 
	{
		return tags;
	}

	public void setTags(List<String> tags) 
	{
		List<String> sortedTags = new ArrayList<>(tags);
		sortedTags.sort((s1, s2) -> s1.compareTo(s2));
		this.tags = sortedTags;
	}
}
