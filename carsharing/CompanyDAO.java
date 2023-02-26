package carsharing;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO implements CompanyModel{
    private static List<Company> companies;
    public CompanyDAO(){
        companies = new ArrayList<>();
    }
    @Override
    public List<Company> complanyList() {
        try{
            PreparedStatement ps =DBconnection.connection.prepareStatement("SELECT * FROM COMPANY");
            ResultSet res = ps.executeQuery();
            while (res.next()){
                companies.add(new Company(res.getInt(1), res.getString(2)));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return companies;
    }
    @Override
    public void addCompany(String name) {
         Query.insertCompanyQuery(name);
    }
    public List<Company> getCompanies() {
        return companies;
    }
    public static int getIdFromList(String companyName){
        int id = 0;
        for(Company company1 : companies){
            if(company1.getName().equals(companyName)){
                id = company1.getId();
                break;
            }
        }
        return  id;
    }
    public static String findCompanyNameWithID(int id){
        String nameSearch = "";
        for(Company company : companies){
            if(company.getId() == id){
                nameSearch = company.getName();
                break;
            }
        }
        return nameSearch;
    }
}
