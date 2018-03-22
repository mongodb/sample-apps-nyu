###Array Query###

Create an Atlas free-tier cluster containing a stores.orders collection in it. Orders should have the following form.
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
Write a Java sample application that queries this collection. The sample app should have the following methods: one that illustrates the use of the $in operator; one that illustrates the use of the $all operator; one that illustrates an equality match on an array field, and one that illustrates the use of the $elemMatch operator.
 Two of the methods should also make use of project.
 Write unit tests for all methods plus one for connecting to the cluster.
 Pull request passes code review and merged.
