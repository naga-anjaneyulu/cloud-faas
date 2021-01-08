from flask import escape
import matplotlib.pyplot as plt
from google.cloud import storage
import io
import json

client = storage.Client(project='naga-kopalle2')
bucket = client.bucket('naga-faas-bucket')


def generate_historgram(histo_list, link):
    plt.hist(histo_list, density=True, bins=30) 
    plt.ylabel('Sentence Length')
    plt.xlabel('Sentence Number');
    plt.title('Length Distribution' + link[-10:-3])
    image = 'histogram_' + str(link[-10:-3]) + 'png'
    blob = bucket.blob(image)

    buf = io.BytesIO()
    plt.savefig(buf, format='png')
    blob.upload_from_string(
        buf.getvalue(),
        content_type='image/png')
    blob.make_public()
    buf.close()
    return blob.public_url


def visualize_data(request):
    
    if request.method == 'OPTIONS':
        headers = {
            'Access-Control-Allow-Origin': '*',
            'Access-Control-Allow-Methods': 'GET',
            'Access-Control-Allow-Headers': 'Content-Type',
            'Access-Control-Max-Age': '3600'}
        return ('', 204, headers)

    
    request_json = request.get_json(silent=True)
    request_args = request.args
    data = {}
    file = ""
    print("JSON request")
    print(request_json)
    for key,value in request_args.items():
        print(key)
    print("JSON request over")
    if request_json and 'data' in request_json:
        data = request_json['data']
        file = request_json['url'].strip()
    elif request_args and 'data' in request_args:
        data = request_args['data']
        file = request_json['url'].strip()
    histo_list = []
    print(data)
    for key,value in data.items():
        histo_list += [int(value)]
    link = generate_historgram(histo_list , file)
    headers = {
            'Access-Control-Allow-Origin': '*',
            'Content-Type': 'application/json'
            }
    return (json.dumps('{"imageSrc":"' + link + '"}'),200,headers)

