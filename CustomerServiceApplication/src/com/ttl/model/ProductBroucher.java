package com.ttl.model;

public class ProductBroucher {
	public String icon;
	public String product;
	public String website;
	public String brochure;
	public String updated_at;
	public String created_at;
	public ProductBroucher() {
		
		
	}

	public ProductBroucher(String icon, String product, String website,
			String brochure) {
		super();
		this.icon = icon;
		this.product = product;
		this.website = website;
		this.brochure = brochure;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBrochure() {
		return brochure;
	}

	public void setBrochure(String brochure) {
		this.brochure = brochure;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}


	
	
	
	
}
