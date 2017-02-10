package com.iqmsoft.cassandra;

import java.util.List;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends CrudRepository<Vendor, String> {

	@Query("Select * from vendor where firstname=?0")
	public Vendor findByFirstName(String firstName);

	@Query("Select * from vendor where lastname=?0")
	public List<Vendor> findByLastName(String lastName);

}

