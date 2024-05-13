package com.dqt.hotel.elk;

import com.dqt.hotel.dto.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/employee-elk")

@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService bookService;

    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeRequest request) {
        try {
            ResponseMessage result = bookService.add(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("add-list")
    public ResponseEntity<?> addEmployees(@RequestBody List<EmployeeRequest> list) {
        try {
            ResponseMessage result = bookService.addList(list);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<?> search(@RequestParam String key) {
        try {
            return ResponseEntity.ok(bookService.findEmployeesByBoolQuery(key));
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> geById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(bookService.getById(id));
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            ResponseMessage result = bookService.getAll();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw e;
        }
    }
}
