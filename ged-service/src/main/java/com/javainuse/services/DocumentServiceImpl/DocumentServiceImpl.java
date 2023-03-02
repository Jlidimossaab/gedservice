package com.javainuse.services.DocumentServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.models.Document;
import com.javainuse.models.LoanOfficer;
import com.javainuse.repositories.DocumentRepository;
import com.javainuse.services.DocumentService;
import com.javainuse.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import java.nio.file.attribute.BasicFileAttributes;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    FileEncryptionService fileEncryptionService;

    @Override
    public void uploadDocument(MultipartFile file, String loanOfficerJson,String category) throws IOException {

        System.out.println("categoory receiived is " + category);

        ObjectMapper mapper = new ObjectMapper();
        LoanOfficer loanOfficer = mapper.readValue(loanOfficerJson, LoanOfficer.class);
        //System.out.println("filenamee is " + file.getOriginalFilename());

        LocalDateTime date = LocalDateTime.now();
        //System.out.println(date);

        List<String> datesList = new ArrayList<>();
        datesList.add(String.valueOf(loanOfficer.getName())); // use 2 digits for day
        String directoryName = Constants.STORAGE_LOCATION;
        for (String folder : datesList) {
            directoryName += folder + "/";

            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }
        }
        //System.out.println("direectooooryy namee == " + directoryName);
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
        Path path = Paths.get(directoryName + "/" + UUID.randomUUID() + extension);
            // Encrypt the file contents
            try {
                byte[] encryptedContents = fileEncryptionService.encrypt(file.getBytes());

                Files.write(path, encryptedContents);
                // set file permission to readable for everyone
                path.toFile().setReadable(true, false);

            } catch (NoSuchPaddingException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e) {
                throw new RuntimeException(e);
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            }



        Document doc = new Document(file.getOriginalFilename(), file.getContentType(), path.toString(), UUID.randomUUID().toString(), date,true,category, loanOfficer);
        documentRepository.save(doc);
    }
    @Override
    public void putDocument(String category) throws IOException {
        if (category!=null){
            List<Document> documents = documentRepository.findByCategory(category).get();
            if(!documents.isEmpty()){
                documents.forEach(document -> {
                    document.setStatus(false);
                    documentRepository.save(document);
                });
            }
        }
        else {
            log.error("category is null");
        }

    }

    @Override
    public ResponseEntity<byte[]> getDocument(Long id) throws IOException {
        System.out.println("id==>" + id);
        final Optional<Document> retrievedDocument = documentRepository.findById(id);
        if (retrievedDocument.isPresent()) {
            System.out.println("path ===>" + retrievedDocument.get().getPath());
            String filePath = retrievedDocument.get().getPath().replace('\\', '/');
            Path path = Paths.get(filePath);

            byte[] documentBytesEncrypted = Files.readAllBytes(path);

            // Decrypt the file contents
            try {
                byte[] decryptedContents = fileEncryptionService.decrypt(documentBytesEncrypted);

                HttpHeaders headers = new HttpHeaders();

                int index = filePath.lastIndexOf(".");

                String fileExtension = "";
                if (index > 0) {
                    fileExtension = filePath.substring(index + 1);
                }
                setContentType(fileExtension ,headers);
                headers.setContentLength(decryptedContents.length);
                return new ResponseEntity<>(decryptedContents, headers, HttpStatus.OK);

            } catch (NoSuchPaddingException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e) {
                throw new RuntimeException(e);
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("document not retrieved from database");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }



    @Override
    public void deleteById(Long id) {
        final Optional<Document> retrievedDocument = documentRepository.findById(id);
        if (retrievedDocument.isPresent()) {
            System.out.println("path ===>" + retrievedDocument.get().getPath());
            String filePath = retrievedDocument.get().getPath().replace('\\', '/');
            System.out.println("doees the file is been selected? lemme see the path at least: " + filePath);
            Path path = Paths.get(filePath);
            try {
                Path parentPath = path.getParent();
                deleteDirectory(parentPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            documentRepository.deleteById(id);
        }
    }

    public static void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }


    void setContentType(String fileExtension,HttpHeaders headers){
        System.out.println("fileExtension=" + fileExtension);
        switch (fileExtension) {
            case "pdf":
                headers.setContentType(MediaType.APPLICATION_PDF);
                break;
            case "docx":
                headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
                break;
            case "doc":
                headers.setContentType(MediaType.valueOf("application/msword"));
                break;
            case "xlsx":
                headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                break;
            case "xls":
                headers.setContentType(MediaType.valueOf("application/vnd.ms-excel"));
                break;
            case "pptx":
                headers.setContentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
                break;
            case "jpeg":
            case "jfif":
                headers.setContentType(MediaType.valueOf("image/jpeg"));
                break;
            case "png":
                headers.setContentType(MediaType.valueOf("image/png"));
                break;
            case "gif":
                headers.setContentType(MediaType.valueOf("image/gif"));
                break;
            case "bmp":
                headers.setContentType(MediaType.valueOf("image/bmp"));
                break;
            case "ppt":
                headers.setContentType(MediaType.valueOf("application/vnd.ms-powerpoint"));
                break;
            default:
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                break;
        }
    }
}
