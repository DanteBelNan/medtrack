#!/bin/bash
echo "Init config of SQS..."
awslocal sqs create-queue --queue-name medtrack-notifications
echo "Queue medtrack-notifications created successfuly."