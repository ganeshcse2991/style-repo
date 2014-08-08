package com.style.webapp.action;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.Preparable;
import com.style.Constants;
import com.style.branch.service.BranchManager;
import com.style.category.service.ProductCategoryManager;
import com.style.exception.AppException;
import com.style.model.Branch;
import com.style.model.ProductCategory;
import com.style.util.StringUtil;

/**
 * Action for facilitating product category management feature.
 * 
 * @auther mathi
 */
public class ProductCategoryAction extends BaseAction implements Preparable {

	private static final long serialVersionUID = 6776558938712115191L;
	private ProductCategoryManager categoryManager;
	private BranchManager branchManager;
	private List<ProductCategory> categories;
	private ProductCategory category;
	private String id;
	private List<Branch> branches;

	@Autowired
	public void setProductCategoryManager(ProductCategoryManager categoryManager) {
		this.categoryManager = categoryManager;
	}

	@Autowired
	public void setBranchManager(BranchManager branchManager) {
		this.branchManager = branchManager;
	}

	@Override
	public void prepare() throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * show category for for create/update
	 * 
	 * @return
	 */
	public String showCategory() {
		log.info("show category");
		if (!StringUtil.isEmptyString(getId())) {
			category = categoryManager.getProductCategory(getId());
		}
		return "success";
	}

	/**
	 * show all categories
	 * 
	 * @return
	 */
	public String showCategories() {
		categories = categoryManager.getAllProductCategoriess();
		return "success";
	}

	/**
	 * save category
	 * 
	 * @return
	 */
	public String saveCategory() {
		log.info("category saving");
		try {
			String[] branchIds = getRequest().getParameterValues("branchIds");
			if (null != branchIds && branchIds.length > 0) {
				branches = branchManager.getBranches(branchIds);
				getCategory().setBranches(new HashSet<Branch>(branches));
				category = categoryManager.saveCategory(getCategory());
				saveMessage("category saved successfully");
				getRequest()
						.getSession()
						.getServletContext()
						.setAttribute(Constants.CATEGORIES,
								categoryManager.getProductCategories());
				return showCategories();
			} else {
				saveMessage("select atleast one branch");
				return "error";
			}
		} catch (AppException e) {
			saveMessage(e.getMessage());
			log.error(e.getMessage(), e);
			return "error";
		}
	}

	public ProductCategory getCategory() {
		return category;
	}

	public void setCategory(ProductCategory category) {
		this.category = category;
	}

	public List<ProductCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<ProductCategory> categories) {
		this.categories = categories;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Branch> getBranches() {
		return branches;
	}

	public void setBranches(List<Branch> branches) {
		this.branches = branches;
	}

}
