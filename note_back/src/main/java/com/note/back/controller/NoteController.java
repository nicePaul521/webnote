package com.note.back.controller;

import com.note.back.entity.Category;
import com.note.back.entity.Note;
import com.note.back.entity.User;
import com.note.back.response.Response;
import com.note.back.service.CategoryService;
import com.note.back.service.NoteService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.rmi.server.ExportException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
public class NoteController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    NoteService noteService;

    /**
     * 获取所有笔记类
     * @return
     */
    @CrossOrigin
    @GetMapping("/api/categories")
    @ResponseBody
    public List<Category> getCategoryList(){
        Subject subject = SecurityUtils.getSubject();
        return categoryService.getAllByUser((User)subject.getPrincipal());
    }

    /**
     * 获取某一类下的所有笔记
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("/api/categories/{id}/notes")
    @ResponseBody
    public List<Note> getNotesByCategory(@PathVariable("id") int id){
        return noteService.getNotesByCategory(id);
    }

    /**
     * 修改笔记的标题与摘要
     * @param requestNote
     * @param id
     * @return
     */
    @CrossOrigin
    @PostMapping("/api/update/{type}/note/{id}")
    @ResponseBody
    public Response updateNoteInfo(@RequestBody Note requestNote,@PathVariable("id") int id,@PathVariable("type") String type){
        Note note = noteService.getById(id);

        if(type.equals("info")){
            note.setName(requestNote.getName());
            note.setAbs(requestNote.getAbs());
        }
        else if(type.equals("content")){
            note.setContentHtml(requestNote.getContentHtml());
            note.setContentMd(requestNote.getContentMd());
        }
        note.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        noteService.updateNote(note);
        return new Response(200,"成功",null);
    }

    /**
     * 添加笔记
     * @param requestNote
     * @param id
     * @return
     */
    @CrossOrigin
    @PostMapping("/api/update/category/{id}/note/add")
    @ResponseBody
    public Response addNode(@RequestBody Note requestNote,@PathVariable("id") int id){
        Subject subject = SecurityUtils.getSubject();
        Note note = new Note();
        note.setName(requestNote.getName());
        note.setAbs(requestNote.getAbs());
        Category category = categoryService.getById(id);
        note.setCategory(category);
        note.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        note.setLastModifiedTime(new Timestamp(System.currentTimeMillis()));
        note.setAuthor((User)subject.getPrincipal());
        noteService.updateNote(note);
        return new Response(200,"成功",null);
    }

    /**
     * 根据id删除笔记种类
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("api/category/delete/{id}")
    @ResponseBody
    public Response deleteCategory(@PathVariable("id") int id){
        categoryService.deleteById(id);
        return new Response(200,"成功",null);
    }

    /**
     * 根据id删除笔记
     * @param id
     * @return
     */
    @CrossOrigin
    @GetMapping("api/note/delete/{id}")
    @ResponseBody
    public Response deleteNote(@PathVariable("id") int id){
        noteService.deleteById(id);
        return new Response(200,"成功",null);
    }

    /**
     * 添加笔记分类
     * @param requestCategory
     * @return
     */
    @CrossOrigin
    @PostMapping("/api/category/add")
    @ResponseBody
    public Response addCategory(@RequestBody Category requestCategory){
        Subject subject = SecurityUtils.getSubject();
        Category category = new Category();
        category.setName(requestCategory.getName());
        category.setAuthor((User)subject.getPrincipal());
        categoryService.updateCategory(category);
        return new Response(200,"成功",null);
    }

    /**
     * 修改笔记分类
     * @param requestCategory
     * @return
     */
    @CrossOrigin
    @PostMapping("/api/category/update")
    @ResponseBody
    public Response updateCategory(@RequestBody Category requestCategory){
        Category category = categoryService.getById(requestCategory.getId());
        category.setName(requestCategory.getName());
        categoryService.updateCategory(category);
        return new Response(200,"成功",null);
    }

    /**
     * 获取笔记内容
     */
    @CrossOrigin
    @GetMapping("/api/note/{id}")
    @ResponseBody
    public Response getNote(@PathVariable("id") int id){
        Note note = noteService.getById(id);
        return new Response(200,"成功",note);
    }

    /**
     * 图片保存到服务器并生成图片链接返回
     */
    final static String PIC_PATH = "static/pics/";

    @CrossOrigin
    @PostMapping("/api/pic")
    @ResponseBody
    public Response uploadPic(MultipartHttpServletRequest mutiRequest, HttpServletRequest request){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datePrefix = dateFormat.format(new Date());
        String savePath = "src/main/resources/" + PIC_PATH;

        File folder = new File(savePath+datePrefix);

        if(!folder.isDirectory()){
            folder.mkdirs();
        }

        String originationalName = mutiRequest.getFile("image").getOriginalFilename();
        String saveName = UUID.randomUUID().toString() + originationalName.substring(originationalName.lastIndexOf("."),originationalName.length());
        String absolutePath = folder.getAbsolutePath();

        try {
            File fileToSave = new File(absolutePath + File.separator + saveName);
            mutiRequest.getFile("image").transferTo(fileToSave);
            String returnPath = request.getScheme() + "://"
                    + request.getServerName()+":"+request.getServerPort()
                    +"/article/images/"
                    + datePrefix +"/"+ saveName;

            return new Response(200,"上传成功",returnPath);

        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response(500,"上传失败",null);
    }

}
