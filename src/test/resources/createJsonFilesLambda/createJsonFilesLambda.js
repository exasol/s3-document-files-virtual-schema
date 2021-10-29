const AWS = require('aws-sdk');

async function doWithRetry(func){
    var retryCounter = 0
    while(true){
        try{
            await func();
            break;
        }catch(exception){
            if(retryCounter < 3){
                await delay(10000)
                console.log("operation failed: " + exception)
                console.log("retrying")
                retryCounter++
            }else{
                throw exception;
            }
        }
    }
}

exports.handler = async (event, context) => {
    const s3 = new AWS.S3();
    var promises = []
    for (var i = 0; i < event.numberOfFiles; i++) {
        var fileId = event.offset + i
        var key = 'test-data-' + fileId + ".json"
        if (event.action == "create") {
            var data = {
                id: fileId,
                name: randomString(20)
            }
            var json_data = JSON.stringify(data)
            const params = {
                Bucket: event.bucket,
                Key: key,
                Body: json_data
            };
            promises.push(doWithRetry(() => s3.upload(params).promise()));
        } else if(event.action == "delete") {
            var params = {  Bucket: event.bucket, Key: key };
            promises.push(doWithRetry(() => s3.deleteObject(params).promise()));
        }
        await delay(5)
    }
    try {
        await Promise.all(promises)
    } catch (exception) {
        console.log("error!")
        console.log(exception)
        context.fail("failed to create s3 object: " + exception)
    }
};

function randomString(length) {
    let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return result;
}

function delay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}