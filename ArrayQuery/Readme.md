### Array Query

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


#### GetShippingByCity - $in Operator

```javascript
List<Document> queryResult = collection.find(in("shippingAddress.city",cities));
```
The $in operator selects the documents where the value of a field equals any value in the specified array.
The above query selects all documents in the *stores.orders* collection where the *shippingAddress.city* field value is either "Floris", "Calpine","Orason" or "Allentown".

If the field *shippingAddress.city* has more than one value, then the $in operator selects all the documents that contains at least one element that matches a value in the *cities* array.


#### GetShippingByItem - $all Operator

```javascript
List<Document> queryResult = collection.find(all("lineitems.name",items));
```
The $all operator selects the documents where the value of a field is an array that contains all the specified elements. 
The above query selects all documents in the *stores.orders* collection where the names of the lineitems filed is "omak 2 Door Mobile Cabinet" and "Glue Sticks".


### FindByStatus - $eq Operator
```javascript
List<Document> queryResult = collection.find(eq("status",status));
```
The $eq operator matches documents where the value of a field equals the specified value.
The above query selects all documents in the *stores.orders* collection where the status of the items equals "shipped". The field *status* is an array that contains the status such as “shipped”, “ordered”, “cancelled” and the date the status has changed. 


### FindByItemQuantity - $elemMatch Operator
```javascript
 List<Document> queryResult = collection.find(elemMatch("lineitems",and
                    (eq("name",item_name),gt("quantity",quantity)))) ;
```

The $elemMatch operator matches documents that contain an array field with at least one element that matches all the specified query criteria.
The above query selects all documents in the *stores.orders* collection where the item is “Glue Sticks” and the quantity is greater than 30.
