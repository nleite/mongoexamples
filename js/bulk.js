function unordered_bulk(db){
    var bulk = db.col.initializeUnorderedBulkOp();
    bulk.insert( {_id:1} );
    bulk.insert( {_id:2} );
    bulk.insert( {_id:3} );
    bulk.insert( {_id:1} );
    bulk.find( {_id:5}).update( { "$set": {"newvalue":true} } );
    bulk.insert( {_id:5} );
}


function ordered_bulk(db){
    var bulk = db.col.initializeOrderedBulkOp();
    bulk.insert( {_id:10} );
    bulk.insert( {_id:12} );
    bulk.find( {_id:9}).update({"$set": {"newvalue":4}});
    bulk.insert( {_id:13} );
    bulk.insert( {_id:10} );
    bulk.find( {_id:12}).update({"$set": {"newvalue":2}});
    bulk.insert( {_id:11} );
    bulk.find( {_id:10}).update({"$set": {"newvalue":11}});
    bulk.insert( {_id:11} );
    bulk.find( {_id:10}).update({"$set": {"newvalue":1}});
}

function clean(db){
    db.col.drop();
}


ordered_bulk(db);
unordered_bulk(db);
clean(db);



