//package com.example.mayoweb.RestController;
//
//import com.example.mayoweb.ErrorMessage;
//import com.example.mayoweb.ResponseWrapper;
//import com.example.mayoweb.Store.StoresService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//@RestController
//@RequestMapping("/api/stores") //값 들어오나 확인용
//public class StoresRestController {
//
//    private final StoresService storesService;
//
//  // @Value("${response.status}")
//    private int statusCode;
//
//  //  @Value("${response.name}")
//    private String name;
//
//    private Object payload;
//    private ResponseWrapper response;
//    private static final String CLASS_NAME = "CategoryService";
//
//    public StoresRestController(StoresService storesService) {
//        this.storesService = storesService;
//        payload = null;
//    }
//
//    @GetMapping("/stores")
//    public ResponseEntity<Map<String,Object>> getStores(){
//        try{
//            payload = storesService.getStores();
//            statusCode = 200;
//            name = "stores";
//        } catch (ExecutionException | InterruptedException e) {
//            payload = new ErrorMessage("Cannot fetch categories from database.",CLASS_NAME, e.toString());
//        }
//        response = new ResponseWrapper(statusCode,name, payload);
//
//        return response.getResponse();
//    }
//}
//
