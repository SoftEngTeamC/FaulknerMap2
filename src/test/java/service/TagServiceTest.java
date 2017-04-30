package service;

import model.Tag;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TagServiceTest {
    TagService tagService = new TagService();
    Tag testTag;
    Long testId = 123456789L;

    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        testTag = new Tag("testTag");
        testTag.setId(testId);
    }

    @Test
    public void find() throws Exception {
        try{
            System.out.print(testTag.getId());
            tagService.find(testTag.getId());
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }
}