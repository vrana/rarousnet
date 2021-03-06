"use strict";

const pulumi = require("@pulumi/pulumi");
const aws = require("@pulumi/aws");

const domain = "www.rarous.net";

const bucket = new aws.s3.Bucket(`${domain}/bucket`, {
  bucket: domain,
  acl: "public-read",
  website: {
    indexDocument: "index.html",
    errorDocument: "404.html"
  }
});

const bucketPolicy = new aws.s3.BucketPolicy(`${domain}/bucket-policy`, {
  bucket: bucket.bucket,
  policy: JSON.stringify({
    Version: "2012-10-17",
    Statement: [
      {
        Sid: "1",
        Effect: "Allow",
        Principal: {
          AWS: "*"
        },
        Action: "s3:GetObject",
        Resource: `arn:aws:s3:::${domain}/*`
      }
    ]
  })
});

exports.bucketUri = bucket.bucket.apply(x => `s3://${x}`);
exports.websiteUri = bucket.websiteEndpoint.apply(x => `http://${x}`);
