'''
Created on Nov 19, 2011
@author: glenn
'''
from flask import Flask, request
from XenServer import Session, Failure
import time
import json

app = Flask(__name__)

def getServerSession():
    serveraddress = request.form['address']
    username = request.form['username']
    password = request.form['password']
    if not serveraddress.startswith("http://"):
        serveraddress = "http://" + serveraddress
    try :
        session = Session(serveraddress)
        session.login_with_password(username, password)
        return session
    except Failure as error:
        return str(error)

@app.route("/mobilexencenter/hosts", methods=['POST'])
def getHostsList():
    if request.method == 'POST':
        hostList = []
        session = getServerSession()
        try :
            hostrecords = session.xenapi.host.get_all_records()
            for hostref in hostrecords.keys():
                hostrec = hostrecords[hostref] 
                hostInfo = dict()
                hostInfo["uuid"] = hostrec["uuid"]
                hostInfo["name"] = hostrec["name_label"]
                hostList.append(hostInfo)
        except Failure as error:
            return str(error)
        return json.dumps(hostList)


@app.route("/mobilexencenter/virtualmachines", methods=['POST'])
def getVMList():
    if request.method == 'POST':
        virtualMachines = []
        session = getServerSession()
        try:
            vmList = session.xenapi.VM.get_all()
            for vm in vmList :
                record = session.xenapi.VM.get_record(vm)
                if not(record["is_a_template"]) and not(record["is_control_domain"]):
                    vmInfo = dict()
                    vmInfo["name"] = record["name_label"]
                    vmInfo["uid"] = record["uuid"]
                    vmInfo['state'] = record["power_state"]
                    virtualMachines.append(vmInfo)
        except Failure as error:
            return str(error)
        return json.dumps(virtualMachines)
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/generalInfo", methods=['POST'])
def getVMInfo(vm_uid):
    if request.method == 'POST':
        vmInfo = dict()
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            vmrec = session.xenapi.VM.get_record(vmref)
            vmInfo["name"] = vmrec["name_label"]
            vmInfo['description'] = vmrec["name_description"]
            vmInfo["memory"] = vmrec["memory_target"]
            vmInfo['vcpuCount'] = vmrec["VCPUs_max"]
        except Failure as error:
            return str(error)
        return json.dumps(vmInfo)
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/start", methods=['POST'])
def startVm(vm_uid):
    if request.method == 'POST':
        result = dict()
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            session.xenapi.Async.VM.start(vmref, False, True)
            task = session.xenapi.Async.VM.start(vmref, False, True)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info'][0]
            result['host'] = session.xenapi.host.get_name_label(task_record['resident_on'])
        except Failure as error:
            return str(error)
        return json.dumps(result)    
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/stop", methods=['POST'])
def stopVm(vm_uid):
    if request.method == 'POST':
        result = dict()
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.clean_shutdown(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info'][0]
        except Failure as error:
            return str(error)
        return json.dumps(result)  
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/resume", methods=['POST'])
def resumeVm(vm_uid):
    if request.method == 'POST':
        result = dict()
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.resume(vmref, False, True)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info'][0]
        except Failure as error:
            return str(error)
        return json.dumps(result)  
 
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/pause", methods=['POST'])
def pause(vm_uid):
    if request.method == 'POST':
        result = dict()
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.pause(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info'][0]
        except Failure as error:
            return str(error)
        return json.dumps(result) 
   
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/unpause", methods=['POST'])
def unpause(vm_uid):
    if request.method == 'POST':
        result = dict()
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.unpause(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info'][0]
        except Failure as error:
            return str(error)
        return json.dumps(result) 
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/suspend", methods=['POST'])
def suspend(vm_uid):
    if request.method == 'POST':
        result = dict()
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.suspend(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info'][0]
        except Failure as error:
            return str(error)
        return json.dumps(result) 
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/memory", methods=['POST'])
def getVmMemoryConfig(vm_uid):
    if request.method == 'POST':
        session = getServerSession()    
        memInfo = dict()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)         
            vmrec = session.xenapi.VM.get_record(vmref)
            memInfo['staticMax'] = vmrec["memory_static_max"]
            memInfo['staticMin'] = vmrec["memory_static_min"]
            memInfo['dynamicMax'] = vmrec["memory_dynamic_max"]
            memInfo['dynamicMin'] = vmrec["memory_dynamic_min"]            
        except Failure as error:
            return str(error)
        return json.dumps(memInfo)

@app.route("/mobilexencenter/virtualmachines/<vm_uid>/memory/update", methods=['POST'])
def updateVmMemoryConfig(vm_uid):
    if request.method == 'POST':
        session = getServerSession()
        try :
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            if request.form.has_key('staticMax') :
                session.xenapi.VM.set_memory_static_max(vmref, request.form['staticMax'])
            if request.form.has_key('staticMin') :
                session.xenapi.VM.set_memory_static_max(vmref, request.form['staticMin'])
            if request.form.has_key('dynamicMax') :
                session.xenapi.VM.set_memory_dynamic_min(vmref, request.form['dynamicMax'])
            if request.form.has_key('dynamicMin') :
                session.xenapi.VM.set_memory_dynamic_max(vmref, request.form['dynamicMin'])
        except Failure as error:
            return str(error)
        return json.dumps("Success")

if __name__ == "__main__":
    app.run(host = "0.0.0.0", port=8000)