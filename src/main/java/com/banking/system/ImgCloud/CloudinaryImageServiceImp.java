package com.banking.system.ImgCloud;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;


@Service
public class CloudinaryImageServiceImp {

	@Autowired
	private Cloudinary cloudinary;
	
	String imageUrl="";
	
	
	public Map upload(MultipartFile file) {
		
		try {
			Map data = this.cloudinary.uploader().upload(file.getBytes(),Map.of());
			 imageUrl=String.valueOf(data.get("url"));
			    System.out.println(imageUrl);
//			    admin.setProfileImgUrl(imageUrl);
			return data;
		} catch (IOException e) {
		  throw new RuntimeException("Image uploading fail !!");
		}
		
	}

}
