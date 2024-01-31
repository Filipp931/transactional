# @Transactional

Demo project for demonstration `@Transactional` mechanism in Spring.

## Basic mechanism

For all public methods, annotated with `@Transactional` Spring creates proxy, using CGLIB (by default).
Proxy mechanism can be represented as follows:
```kotlin
val transaction = entityManager.getTransaction(); 

try { 
    transaction.begin()
    invoke_delegate_method()
    transaction.commit()
} catch(e: RuntimeException) { 
    transaction.rollback()
    throw e
}
```

## Main principles

#### 1. Default transaction rollback only for `RuntimeException`
```kotlin
@Transactional
fun transactionalMethod() {
    saveSomething()
    throw Exception()
}
```
In above example transaction won't rollback. Be careful using Kotlin.

#### 2. We can customize rollback for required exceptions manually
```kotlin
@Transactional(rollbackFor = [Exception::class])
fun transactionalMethod() {
    saveSomething()
    throw Exception()
}
```
#### 3. Also we can disable rollback for required exceptions
```kotlin
@Transactional(noRollbackFor = [RuntimeException::class])
fun transactionalMethod() {
    saveSomething()
    throw Exception()
}
```
#### 4. By default propagation set up to `Propagation.REQUIRED`
It means that if the transaction is already opened somewhere externally, no new transaction will be created - an existing transaction will be reused.
```kotlin
//Service1
@Transactional
fun transactionalMethod1() {
    service2.transactionalMethod2()
    saveSomething()
}

//Service2
@Transactional
fun transactionalMethod2() {
    throw Exception()
}
```
In above example only one transaction will be created.

#### 5. We can set up propagation to `Propagation.REQUIRES_NEW` if there is need to execute `service2.transactionalMethod2()` in separate transaction
```kotlin
//Service1
@Transactional
fun transactionalMethod1() {
    service2.transactionalMethod2()
    saveSomething()
}

//Service2
@Transactional(propagation = Propagation.REQUIRES_NEW)
fun transactionalMethod2() {
    throw Exception()
}
```
