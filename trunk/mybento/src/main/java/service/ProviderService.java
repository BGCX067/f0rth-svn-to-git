package service;

import java.util.Date;

import model.Product;
import model.Provider;

import org.springframework.transaction.annotation.Transactional;

import dao.ProductDao;
import dao.ProviderDao;
import dto.ProductData;
import dto.ProviderData;

@Transactional
public class ProviderService {
	private ProviderDao providerDao;

	private ProductDao productDao;

	public void setProviderDao(ProviderDao providerDao) {
		this.providerDao = providerDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public Provider createProvider(Provider provider) {
		Date now = new Date();
		provider.setCreatedAt(now);
		provider.setUpdatedAt(now);
		providerDao.save(provider);
		return provider;
	}

	public void deleteProvider(Provider provider) {
		providerDao.delete(provider);
	}

	public ProviderData getProviderData(Long id) {
		return new ProviderData(providerDao.load(id));
	}

	public void saveProviderData(ProviderData data) {
		// delete products
		for (ProductData a : data.getProducts()) {
			if (a.isSelected()) {
				productDao.delete(productDao.load(a.getProductId()));
			} else {
				Product product = productDao.load(a.getProductId());
				product.setName(a.getName());
				product.setPrice(a.getPrice());
			}
		}
		// update provider
		Provider provider = providerDao.load(data.getProviderId());
		provider.setName(data.getName());
		provider.setPhone(data.getPhone());
		provider.setAddress(data.getAddress());
		provider.setUpdatedAt(new Date());
		// create new product
		if (data.getNewProduct() != null) {
			provider.addProduct(data.getNewProduct().getName(),
								data.getNewProduct().getPrice());
		}
	}
}
