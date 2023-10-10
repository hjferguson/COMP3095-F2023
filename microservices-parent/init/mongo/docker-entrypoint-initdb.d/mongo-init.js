//log the start
print('START');
//switch to the 'product-service' database (or create it if it doesn't exist)
db = db.getSiblingDB('product-service');

db.createUser({
    user: 'rootadmin',
    pwd: 'password',
    roles: [{role: 'readWrite', db: 'product-service'}],
});

db.createCollection('user');

//log the end of the script execution
print('END');

