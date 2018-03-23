# MongoDB Java Driver Find Method with Scalar Fields

This is a sample application that demonstrates MongoDB Java Driver's find method. It has four methods:

* one that illustrates a simple equality match 
* one that illustrates an equality match on two or more fields
* one that illustrates a range query on order total 
* one that illustrates a range query by date.

The last two methods also make use of 'project'.

## Getting Started

The sample application queries data from MongoDB Atlas free-tier cluster. The cluster contains stores.orders collection which mimics an e-commerce database. The general schema is as follows: 

```
{
  _id: ObjectId(),
  orderPlaced: datetime(),
  total: NumberDecimal("153.00"),
  subtotal: NumberDecimal("141.00"),
  shipping: NumberDecimal("5.00"),
  tax: NumberDecimal("7.00"),
  status: ["shipped", datetime()],
  shippingAddress: {
    number: 345,
    street: Alvin St.,
    city: Madison
    state: WI,
    country: USA
    postalCode: 53558
  },
  lineitems: [
    { sku: "MDBTS001",
      name: "Flannel T-shirt",
      quantity: 10,
      unit_price: NumberDecimal("9.00") },
    { sku: "MDBTS002",
      quantity: 5,
      unit_price: NumberDecimal("10.00")}] 
}
```




