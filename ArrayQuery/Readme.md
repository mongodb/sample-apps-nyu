###Array Query

The collection **stores.orders** have the following form.
```javascript
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



```javascript
List<Document> queryResult = collection.find(in("shippingAddress.city",cities))
```
The $in operator selects the documents where the value of the field *shippingAddress.city* equals any value in the *cities* array. 
If the field *shippingAddress.city* has more than one value, then the $in operator selects all the documents that contains at least one element that matches a value in the *cities* array 
