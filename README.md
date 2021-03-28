### Simple RPC
See method : getRate();  
Implemented with BockingStub.  
`rpc getRate(RateRequest) returns (CurrencyRate)`  
Pass an instance of RateRequest and get single result as CurrencyRate

### Server-side Streaming RPC
See method : getRateList();  
Implemented with Stub (async).  
`rpc getRateList(RateRequest) returns (stream CurrencyRate)`  
Pass an instance of RateRequest and as a result get a stream of CurrencyRate instances  
As its asynchronous, CountDownLatch used tp determine if stream is finished.  

### Client-side Streaming RPC
See method: getMonthAverage();  
Implemented with Stub (async).  
`rpc getAverageRate(stream RateRequest) returns (CurrencyRate)`  
On server side this method will be generated : public StreamObserver\<RateRequest> getAverageRate(StreamObserver\<CurrencyRate> responseObserver)  
Input parameter is actually a response. Implement `onNext` to get value.  
Return value of type "StreamObserver\<RateRequest>" - is a stream of input values from the client. To pass values put as
much as you need on `onNext`, and `onCompleted` when finished.  

### Bidirectional Streaming RPC
Both sides send a sequence of messages using a read-write stream.
The two streams operate independently, so clients and servers can read and write in whatever order they like: for example,
the server could wait to receive all the client messages before writing its responses, or it could alternately read a
message then write a message, or some other combination of reads and writes. The order of messages in each stream is preserved.

