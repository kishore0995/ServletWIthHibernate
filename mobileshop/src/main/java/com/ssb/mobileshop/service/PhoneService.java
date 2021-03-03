package com.ssb.mobileshop.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ssb.mobileshop.controller.PhoneController;
import com.ssb.mobileshop.dao.PhoneDao;
import com.ssb.mobileshop.model.Phone;

public class PhoneService extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = -4069945438113876806L;

    private static final String model = "/user/model";
    public static int id = 0;
    public static int stock = 0;
    public static float price = 0.0f;
    public static String brand = null;
    public static String modelName = null;
    public float getByPrice;

    private static PhoneService phoneService;

    public static PhoneService getInstance() {
        if (phoneService == null) {
            phoneService = new PhoneService();
        }
        return phoneService;
    }

    public List<Phone> viewPhone() {
        try {
            return PhoneDao.getInstance().Phone();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public List<String> brandName() {
        try {
            return PhoneDao.getInstance().getAllBrands();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public List<Phone> getByBrand(String brand) {
        try {
            return PhoneDao.getInstance().getByBrand(brand);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public Map<String, Integer> getPrice(String modelname) {
        try {
            return PhoneDao.getInstance().getPrice(modelname);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public List<Phone> getByRam(int ram) {
        try {
            return PhoneDao.getInstance().getByRam(ram);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int save(Phone phone) {
        try {
            PhoneDao.getInstance().add(phone);
            return 1;
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    public Phone edit(int id) {
        try {
            return PhoneDao.getInstance().getDetails(id);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public int delete(int id) {
        try {
            return PhoneDao.getInstance().delete(id);
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String path = request.getServletPath();
        switch (path) {
            case model:
                modelName = request.getParameter("modelName");
                System.out.println(modelName);
                Map<String, Integer> priceDetails = getPrice(modelName);
                try {
                    if (!priceDetails.isEmpty()) {
                        for (Entry<String, Integer> entry : priceDetails.entrySet()) {
                            price = Float.parseFloat(entry.getKey());
                            stock = entry.getValue();
                        }

                        if (stock != 0 && price != 0) {
                            out.print("<br>");
                            out.print("<h2>Do you Like to Buy</h2>");
                            out.println("Model " + modelName);
                            out.println("<br>");
                            out.println("Total price of mobile is: " + price);
                            out.print("<br>");
                            out.print("<a href='yes'>Yes</a><br>");
                            out.print("<a href='no'>No</a>");
                        }
                    } else {
                        out.print("<font color=red>Sorry No Phones AVailable for This Model</font>");
                        out.print("<br>");
                        out.print("<a href='viewphone'>ok</a>");
                    }
                } catch (Exception e) {
                    out.print("<font color=red>Sorry No Phones AVailable for This Model</font>");
                    out.print("<br>");
                    out.print("<a href='viewphone'>ok</a>");
                }
                break;

            // Use to filter Phone By Ram and Brand Name
            case "/user/rambrand":
                brand = request.getParameter("brandName");
                int ram = PhoneController.ram;
                if (brand.equals("")) {
                    out.print("<h2>Please Enter Brand Name</h2><br><br>");
                    out.print("<a href='/mobileshop/user/viewbyram?ram=" + ram + "'>ok</a>");
                } else {
                    try {
                        List<Phone> getByRam = PhoneDao.getInstance().getByRamAndBrand(brand, ram);
                        if (!getByRam.isEmpty()) {
                            out.print("<table border='1' width='100%'");
                            out.print(
                                    "<tr><th>Id</th><th>Brand Name</th><th>Model Name</th><th>Ram Size</th><th>Rom Size</th><th>Price</th></tr>");
                            for (Phone list : getByRam) {
                                out.print("<tr><td>" + list.getId() + "</td><td>" + list.getBrandName() + "</td><td>"
                                        + list.getModelName() + "</td><td>" + list.getRam() + "</td><td>"
                                        + list.getRom() + "</td><td>" + list.getPrice() + "</td></tr>");
                            }
                            out.print("<br>");
                            out.print("<html><body>");
                            out.print("<form action='id'>");
                            out.print("<input type='text' name='id' placeholder='Enter Id'>");
                            out.print("<input type='submit' value='search'><br><br>");
                            out.print("<a href='" + request.getContextPath() + "/user'>Home</a>");
                            out.print("</form>");
                            out.print("</body></html>");
                        } else {
                            out.print("<h2>Sorry No Phones Available for Selected brand</h2><br><br>");
                            out.print("<a href='/mobileshop/user/viewbyram?ram=" + ram + "'>ok</a>");
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                break;

            // This api will get the Available Brands Enter By the user
            case "/user/brand":
                brand = request.getParameter("brandName");
                if (brand.equals("")) {
                    out.print("<font color=red>Brand Name Should Not be Empty</font><br><br>");
                    out.print("<a href='viewbrand'>ok</a>");
                } else {
                    try {
                        List<Phone> getByBrand = PhoneDao.getInstance().getByBrand(brand);
                        if (getByBrand.isEmpty()) {
                            out.print("<h2>Sorry No Phones Available</h2><br>");
                            out.print("<a href='viewbrand'>ok</a>");
                        } else {
                            out.print("<table border='1' width='100%'");
                            out.print(
                                    "<tr><th>Id</th><th>Brand Name</th><th>Model Name</th><th>Ram Size</th><th>Rom Size</th><th>Price</th></tr>");
                            for (Phone list : getByBrand) {
                                out.print("<tr><td>" + list.getId() + "</td><td>" + list.getBrandName() + "</td><td>"
                                        + list.getModelName() + "</td><td>" + list.getRam() + "</td><td>"
                                        + list.getRom() + "</td><td>" + list.getPrice() + "</td></tr>");
                            }
                            out.print("<br>");
                            out.print("<html><body>");
                            out.print("<form action='id'>");
                            out.print("<input type='text' name='id' placeholder='Enter Id'>");
                            out.print("<input type='submit' value='search'><br><br>");
                            out.print("<a href='" + request.getContextPath() + "/user'>Home</a>");
                            out.print("</form>");
                            out.print("</body></html>");

                        }

                    } catch (Exception e) {
                        out.print("<font color=red>Enter Valid Brand Name</font><br><br>");
                        out.print("<a href='viewbrand'>ok</a>");
                    }
                }
                break;

            case "/user/viewbyprice":
                try {
                    getByPrice = Float.parseFloat(request.getParameter("price"));
                    List<Phone> detailsByPrice = PhoneDao.getInstance().getByPrice(getByPrice);
                    if (detailsByPrice.isEmpty()) {
                        out.print("<h2>Sorry No Phones Available for This Price Range</h2>");
                    } else {
                        out.print("<table border='1' width='100%'");
                        out.print(
                                "<tr><th>Brand Name</th><th>Model Name</th><th>Ram Size</th><th>Rom Size</th><th>Price</th></tr>");
                        for (Phone list : detailsByPrice) {
                            out.print("<tr><td>" + list.getBrandName() + "</td><td>" + list.getModelName() + "</td><td>"
                                    + list.getRam() + "</td><td>" + list.getRom() + "</td><td>" + list.getPrice()
                                    + "</td></tr>");
                        }
                        out.print("<br>");
                        out.print("<html><body>");
                        out.print("<form action='viewByPriceAndBrand'>");
                        out.print("<input type='text' name='brandName' placeholder='Enter Brand Name'>");
                        out.print("<input type='submit' value='search'><br><br>");
                        out.print("<a href='" + request.getContextPath() + "/user'>Home</a>");
                        out.print("</form>");
                        out.print("</body></html>");
                    }
                } catch (Exception e) {
                    out.print("<font color=red>Enter Valid Price Range</font><br><br>");
                    out.print("<a href='price'>ok</a>");
                }
                break;

            case "/user/id":
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    Phone list = PhoneDao.getInstance().getDetails(brand, id);
                    if (list.getRom() != 0) {
                        out.print("<table border='1' width='100%'");
                        out.print(
                                "<tr><th>Brand Name</th><th>Model Name</th><th>Ram Size</th><th>Rom Size</th><th>Price</th></tr>");
                        out.print("<tr><td>" + list.getBrandName() + "</td><td>" + list.getModelName() + "</td><td>"
                                + list.getRam() + "</td><td>" + list.getRom() + "</td><td>" + list.getPrice()
                                + "</td></tr>");

                        out.print("<br>");
                        out.print("<html><body>");
                        out.print("<form action='buy'>");
                        out.print("<input type='text' name='modelName' placeholder='Enter Model Name'>");
                        out.print("<input type='submit' value='search'><br><br>");
                        out.print("<a href='" + request.getContextPath() + "/user'>Home</a>");
                        out.print("</form>");
                        out.print("</body></html>");

                    } else {
                        out.print("<font color=red>Inavlid Id</font><br><br>");
                        out.print("<a href='/mobileshop/user/brand?brandName=" + brand + "'>ok</a>");
                    }
                } catch (Exception e) {
                    out.print("<font color=red>Invalid Id</font>");
                    out.print("<a href='/mobileshop/user/brand?brandName=" + brand + "'>ok</a>");
                }
                break;

            case "/user/buy":
                modelName = request.getParameter("modelName");
                System.out.println(modelName);
                try {
                    System.out.println(id);
                    Phone list = PhoneDao.getInstance().getByIdAndModel(modelName, id);
                    System.out.println(list.getRom());
                    if (list.getRom() != 0) {
                        out.print("<table border='1' width='100%'");
                        out.print(
                                "<tr><th>Brand Name</th><th>Model Name</th><th>Ram Size</th><th>Rom Size</th><th>Price</th></tr>");
                        out.print("<tr><td>" + list.getBrandName() + "</td><td>" + list.getModelName() + "</td><td>"
                                + list.getRam() + "</td><td>" + list.getRom() + "</td><td>" + list.getPrice()
                                + "</td></tr>");
                        Map<String, Integer> phoneDetails = getPrice(modelName);
                        for (Entry<String, Integer> entry : phoneDetails.entrySet()) {
                            price = Float.parseFloat(entry.getKey());
                            stock = entry.getValue();
                        }
                        if (stock != 0 && price != 0) {
                            out.print("<h2>Do you Like to Buy</h2>");
                            out.println("Model " + modelName);
                            out.println("<br>");
                            out.println("Total price of mobile is: " + price);
                            out.print("<br>");
                            out.print("<a href='yes'>Yes</a><br>");
                            out.print("<a href='no'>No</a>");
                        } else {
                            out.print("<p>Sorry!! no stock available</p>");
                        }
                    } else {
                        out.print("<font color=red>Invalid Model Name</font><br><br>");
                        out.print("<a href='/mobileshop/user/id?id=" + id + "'>ok</a>");
                    }
                } catch (Exception e) {
                    out.print("<font color=red>Invalid Model Name</font><br><br>");
                    out.print("<a href='/mobileshop/user/id?id=" + id + "'>ok</a>");
                }
                break;

            case "/user/viewByPriceAndBrand":
                try {
                    brand = request.getParameter("brandName");
                    List<Phone> detailsByPriceAndBrand = PhoneDao.getInstance().getByPriceAndBrand(getByPrice, brand);
                    if (!detailsByPriceAndBrand.isEmpty()) {
                        out.print("<table border='1' width='100%'");
                        out.print(
                                "<tr><th>Id</th><th>Brand Name</th><th>Model Name</th><th>Ram Size</th><th>Rom Size</th><th>Price</th></tr>");
                        for (Phone list : detailsByPriceAndBrand) {
                            out.print("<tr><td>" + list.getId() + "</td><td>" + list.getBrandName() + "</td><td>"
                                    + list.getModelName() + "</td><td>" + list.getRam() + "</td><td>" + list.getRom()
                                    + "</td><td>" + list.getPrice() + "</td></tr>");
                        }
                        out.print("<br>");
                        out.print("<html><body>");
                        out.print("<form action='id'>");
                        out.print("<input type='text' name='id' placeholder='Enter Id'>");
                        out.print("<input type='submit' value='search'><br><br>");
                        out.print("<a href='" + request.getContextPath() + "/user'>Home</a>");
                        out.print("</form>");
                        out.print("</body></html>");
                    } else {
                        out.print("<h2>Sorry No Phones Available</h2>");
                        out.print("<a href='/mobileshop/user/viewByPriceAndBrand?brandName=" + brand + "'>ok</a>");
                    }

                } catch (Exception e) {

                }
                break;
        }

    }

}
