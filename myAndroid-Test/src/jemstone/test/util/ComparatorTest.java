package jemstone.test.util;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jemstone.model.Entity;
import jemstone.util.compare.CompareException;
import jemstone.util.compare.EntityComparator;
import jemstone.util.compare.ListComparator;
import android.test.AndroidTestCase;

public class ComparatorTest extends AndroidTestCase {
  public void testCategory() throws CompareException, AssertionError {
    CategoryComparator comparator = new CategoryComparator();

    Category cat1 = new Category("Food");
    Category cat2 = new Category("House");

    comparator.assertEquals(cat1, cat1);
    comparator.assertNotEquals(cat1, cat2);
    comparator.assertNotEquals(cat1, null);
  }

  public void testList() throws CompareException, AssertionError {
    ListComparator<Category> comparator = new ListComparator<Category>("Categories", new CategoryComparator());

    List<Category> list1 = new ArrayList<Category>();
    list1.add(new Category("Food"));
    list1.add(new Category("House"));
    list1.add(new Category("Car"));
    
    List<Category> list2 = new ArrayList<Category>(list1);

    comparator.assertEquals(list1, list2);

    list1.add(new Category("Groceries"));

    comparator.assertNotEquals(list1, list2);
  }

  private static int categoryId = 0;
  
  private class Category extends Entity {
    private String name;

    public Category(String name) {
      super(++categoryId);
      this.name = name;
    }

    @Override
    public void print(PrintWriter out, int depth) {
    }
  }
  
  private class CategoryComparator extends EntityComparator<Category> {
    @Override
    public boolean equals(Category cat1, Category cat2) throws CompareException {
      checkNull("Category", cat1, cat2);
      return equals("name", cat1.name, cat2.name);
    }
  }
}
