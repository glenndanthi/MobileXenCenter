'''
Created on Nov 19, 2011
@author: glenn
'''
from flask import Flask, request
import json
app = Flask(__name__)

errorcodes = {1 :"Access denied", 2:"Server not found"}

@app.route("/mobilexencenter/virtualmachines/", methods=['POST'])
def getvmlist():
    if request.method == 'POST':
        serveraddress = request.form['address']
        username = request.form['username']
        password = request.form['password']
        if serveraddress == "192.168.100.100":
            if username == "ramayan" and password == "heyram":
                vmlist = ["Virtual Machine 1","Virtual Machine 2", "Virtual Machine 3"]
                return json.dumps(vmlist)
            else :
                return errorcodes[1]
        else :
            return errorcodes[2]
if __name__ == "__main__":
    app.debug = 'true'
    app.run(host = "0.0.0.0", port=8000)