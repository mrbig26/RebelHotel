// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.file;

import edu.unlv.cs.rebelhotel.file.FileUploadDataOnDemand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

privileged aspect FileUploadIntegrationTest_Roo_IntegrationTest {
    
    declare @type: FileUploadIntegrationTest: @RunWith(SpringJUnit4ClassRunner.class);
    
    declare @type: FileUploadIntegrationTest: @ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml");
    
    declare @type: FileUploadIntegrationTest: @Transactional;
    
    @Autowired
    private FileUploadDataOnDemand FileUploadIntegrationTest.dod;
    
    @Test
    public void FileUploadIntegrationTest.testCountFileUploads() {
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", dod.getRandomFileUpload());
        long count = edu.unlv.cs.rebelhotel.file.UploadProgress.countFileUploads();
        org.junit.Assert.assertTrue("Counter for 'FileUpload' incorrectly reported there were no entries", count > 0);
    }
    
    @Test
    public void FileUploadIntegrationTest.testFindFileUpload() {
        edu.unlv.cs.rebelhotel.file.UploadProgress obj = dod.getRandomFileUpload();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to provide an identifier", id);
        obj = edu.unlv.cs.rebelhotel.file.UploadProgress.findFileUpload(id);
        org.junit.Assert.assertNotNull("Find method for 'FileUpload' illegally returned null for id '" + id + "'", obj);
        org.junit.Assert.assertEquals("Find method for 'FileUpload' returned the incorrect identifier", id, obj.getId());
    }
    
    @Test
    public void FileUploadIntegrationTest.testFindAllFileUploads() {
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", dod.getRandomFileUpload());
        long count = edu.unlv.cs.rebelhotel.file.UploadProgress.countFileUploads();
        org.junit.Assert.assertTrue("Too expensive to perform a find all test for 'FileUpload', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        java.util.List<edu.unlv.cs.rebelhotel.file.UploadProgress> result = edu.unlv.cs.rebelhotel.file.UploadProgress.findAllFileUploads();
        org.junit.Assert.assertNotNull("Find all method for 'FileUpload' illegally returned null", result);
        org.junit.Assert.assertTrue("Find all method for 'FileUpload' failed to return any data", result.size() > 0);
    }
    
    @Test
    public void FileUploadIntegrationTest.testFindFileUploadEntries() {
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", dod.getRandomFileUpload());
        long count = edu.unlv.cs.rebelhotel.file.UploadProgress.countFileUploads();
        if (count > 20) count = 20;
        java.util.List<edu.unlv.cs.rebelhotel.file.UploadProgress> result = edu.unlv.cs.rebelhotel.file.UploadProgress.findFileUploadEntries(0, (int) count);
        org.junit.Assert.assertNotNull("Find entries method for 'FileUpload' illegally returned null", result);
        org.junit.Assert.assertEquals("Find entries method for 'FileUpload' returned an incorrect number of entries", count, result.size());
    }
    
    @Test
    public void FileUploadIntegrationTest.testFlush() {
        edu.unlv.cs.rebelhotel.file.UploadProgress obj = dod.getRandomFileUpload();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to provide an identifier", id);
        obj = edu.unlv.cs.rebelhotel.file.UploadProgress.findFileUpload(id);
        org.junit.Assert.assertNotNull("Find method for 'FileUpload' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyFileUpload(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        obj.flush();
        org.junit.Assert.assertTrue("Version for 'FileUpload' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FileUploadIntegrationTest.testMerge() {
        edu.unlv.cs.rebelhotel.file.UploadProgress obj = dod.getRandomFileUpload();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to provide an identifier", id);
        obj = edu.unlv.cs.rebelhotel.file.UploadProgress.findFileUpload(id);
        boolean modified =  dod.modifyFileUpload(obj);
        java.lang.Integer currentVersion = obj.getVersion();
        edu.unlv.cs.rebelhotel.file.UploadProgress merged = (edu.unlv.cs.rebelhotel.file.UploadProgress) obj.merge();
        obj.flush();
        org.junit.Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        org.junit.Assert.assertTrue("Version for 'FileUpload' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }
    
    @Test
    public void FileUploadIntegrationTest.testPersist() {
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", dod.getRandomFileUpload());
        edu.unlv.cs.rebelhotel.file.UploadProgress obj = dod.getNewTransientFileUpload(Integer.MAX_VALUE);
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to provide a new transient entity", obj);
        org.junit.Assert.assertNull("Expected 'FileUpload' identifier to be null", obj.getId());
        obj.persist();
        obj.flush();
        org.junit.Assert.assertNotNull("Expected 'FileUpload' identifier to no longer be null", obj.getId());
    }
    
    @Test
    public void FileUploadIntegrationTest.testRemove() {
        edu.unlv.cs.rebelhotel.file.UploadProgress obj = dod.getRandomFileUpload();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to initialize correctly", obj);
        java.lang.Long id = obj.getId();
        org.junit.Assert.assertNotNull("Data on demand for 'FileUpload' failed to provide an identifier", id);
        obj = edu.unlv.cs.rebelhotel.file.UploadProgress.findFileUpload(id);
        obj.remove();
        obj.flush();
        org.junit.Assert.assertNull("Failed to remove 'FileUpload' with identifier '" + id + "'", edu.unlv.cs.rebelhotel.file.UploadProgress.findFileUpload(id));
    }
    
}