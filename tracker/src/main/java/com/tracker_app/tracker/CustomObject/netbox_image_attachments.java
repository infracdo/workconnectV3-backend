package com.tracker_app.tracker.CustomObject;

import org.springframework.web.multipart.MultipartFile;

public class netbox_image_attachments {
    //private MultipartFile image;
    private String content_type;
    //private String name;
    private Integer object_id;
    private Integer image_height;
    private Integer image_width;

    /*
    public MultipartFile getimage(){
        return image;
    }
    */
    public String getcontent_type() {
        return content_type;
    }
    /*
    public String getname() {
        return name;
    }
    */
    public Integer getobject_id() {
        return object_id;
    }
    public Integer getimage_height() {
        return image_height;
    }
    public Integer getimage_width() {
        return image_width;
    }
/*
    public void setcontent_type(String content_type){
        this.content_type = content_type;
    }
    public void setobject_id(Integer object_id){
        this.object_id = object_id;
    }
    public void setimage_height(Integer image_height){
        this.image_height = image_height;
    }
    public void setimage_width(Integer image_width){
        this.image_width = image_width;
    }
*/
}
