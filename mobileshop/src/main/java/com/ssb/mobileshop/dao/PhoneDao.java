package com.ssb.mobileshop.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssb.mobileshop.dao.PhoneDao;
import com.ssb.mobileshop.model.Phone;
import com.ssb.mobileshop.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PhoneDao {
    private static PhoneDao PhoneDaoImpl;

    // create singleton for PhoneDaoImpl
    public static PhoneDao getInstance() {
        if (PhoneDaoImpl == null) {
            PhoneDaoImpl = new PhoneDao();
        }
        return PhoneDaoImpl;
    }

    // list Phone
    public List<Phone> Phone() {
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery("from Phone");
            List<Phone> phones = query.list();
            return phones;
        } catch (Exception e) {
            System.out.println(e);
            session.close();
            return null;
        }
    }

    // add Phone to database

    public int add(Phone phone) throws Exception {
        Session session = HibernateUtil.createSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.save(phone);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            session.beginTransaction().rollback();
            return 0;
        }
    }

    // Remove Phone from database
    public int delete(int id) throws Exception {
        Session session = HibernateUtil.createSession();
        try {
            Phone Phone = session.find(Phone.class, id);
            Transaction transaction = session.beginTransaction();
            session.delete(Phone);
            transaction.commit();
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            session.beginTransaction().rollback();
            return 0;
        } finally {
            session.close();
        }

    }

    // Getting Phone details By Id:
    public Phone getDetails(int id) throws Exception {
        Session session = HibernateUtil.createSession();
        try {
            Phone Phone = session.get(Phone.class, id);
            return Phone;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            session.close();
        }
    }

    // Update Price Details of Phone
    public int editPrice(float price, int id) throws SQLException {
        String sql = "update Phone set price=:price where id=:id";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("price", price);
            query.setParameter("id", id);
            int status = query.executeUpdate();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            session.close();
        }
    }

    // update Stock Details of Phone
    public int editByStock(int stock, int id) throws Exception {
        String sql = "update Phone set stock=:stock where id=:id";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("stock", stock);
            query.setParameter("id", id);
            int status = query.executeUpdate();
            return status;
        } catch (Exception e) {
            return 0;
        } finally {
            session.close();
        }
    }

    // Filtering Phone only by Brand
    public List<String> getAllBrands() throws Exception {
        String sql = "select distinct brandName from Phone";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            List<String> brandname = query.list();
            return brandname;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    // Getting Phone Details by Brand Name
    public List<Phone> getByBrand(String brand) throws Exception {
        String sql = "from Phone where brandName like :brand";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("brand", brand + "%");
            List<Phone> brandDetails = query.list();
            return brandDetails;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            session.close();
        }

    }

    // Geting price details and stock by Model Name
    public Map<String, Integer> getPrice(String modelname) throws Exception {
        String sql = "from Phone where modelName=:modelname";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("modelname", modelname);
            Phone phone = (Phone) query.uniqueResult();
            String price = Float.toString(phone.getPrice());
            int stock = phone.getStock();
            Map<String, Integer> buyPhone = new HashMap<>();
            buyPhone.put(price, stock);
            System.out.println(price);
            System.out.println(stock);
            System.out.println(buyPhone);
            return buyPhone;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        } finally {
            session.close();
        }

    }

    // Update stock after Buying a Phone
    public int updateStock(int stock, String modelname) throws Exception {
        String sql = "update Phone set stock=:stock where modelName=:modelname";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("stock", stock);
            query.setParameter("modelname", modelname);
            Transaction transaction = session.beginTransaction();
            int status = query.executeUpdate();
            transaction.commit();
            return status;
        } catch (Exception e) {
            System.out.println(e);
            session.beginTransaction().rollback();
            return 0;
        } finally {
            session.close();
        }
    }

    // Get Phone Details by RAM
    public List<Phone> getByRam(int ram) throws Exception {
        String sql = "from Phone where ram=:ram";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("ram", ram);
            List<Phone> phone = query.list();
            return phone;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            session.close();
        }
    }

    // Get details of Phone by price
    public List<Phone> getByPrice(float price) throws Exception {
        String sql = "from Phone where price <:price";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("price", price);
            List<Phone> byPrice = query.list();
            return byPrice;
        } catch (Exception e) {
            return null;
        } finally {
            session.close();
        }
    }

    // Get details of Phone by both Ram and Brand Name
    public List<Phone> getByRamAndBrand(String brand, int ram) throws Exception {
        String sql = "from Phone where brandName like :brand And ram=:ram";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("brand", brand + "%");
            query.setParameter("ram", ram);
            List<Phone> brandDteails = query.list();
            return brandDteails;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            session.close();
        }
    }

    public Phone getByIdAndModel(String model, int id) throws SQLException {
        String sql = "from Phone where modelName =:model and id=:id";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("model", model);
            query.setParameter("id", id);
            Phone list = (Phone) query.uniqueResult();
            return list;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            session.close();
        }
    }

    public Phone getDetails(String brand, int id) throws SQLException {
        String sql = "from Phone where brandName like :brand And id=:id";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("brand", brand + "%");
            query.setParameter("id", id);
            Phone phoneDetails = (Phone) query.uniqueResult();
            return phoneDetails;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        } finally {
            session.close();
        }

    }

    public int update(Phone phone) {
        Session session = HibernateUtil.createSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.update(phone);
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e);
            session.beginTransaction().rollback();
            return 0;
        } finally {
            session.close();
        }
        return 1;
    }

    public List<Phone> getByPriceAndBrand(float price, String brand) {
        List<Phone> priceAndBrand = new ArrayList<>();
        String sql = "from Phone where brandName like :brand and price <:price";
        Session session = HibernateUtil.createSession();
        try {
            Query query = session.createQuery(sql);
            query.setParameter("brand", brand + "%");
            query.setParameter("price", price);
            priceAndBrand = query.list();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return priceAndBrand;
    }

}