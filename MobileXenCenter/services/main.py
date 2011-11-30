'''
Created on Nov 19, 2011
@author: glenn
'''
from flask import Flask, request
from XenServer import Session, Failure
import time
import json
import provision

app = Flask(__name__)

def getServerSession():
    serveraddress = request.form['address']
    username = request.form['username']
    password = request.form['password']
    if not serveraddress.startswith("http://"):
        serveraddress = "http://" + serveraddress
    session = Session(serveraddress)
    session.login_with_password(username, password)
    return session

@app.route("/mobilexencenter/hosts", methods=['POST'])
def getHostsList():
    if request.method == 'POST':
        hostList = []
        try :
            session = getServerSession()
            hostrecords = session.xenapi.host.get_all_records()
            for hostref in hostrecords.keys():
                hostrec = hostrecords[hostref] 
                hostInfo = dict()
                hostInfo["uuid"] = hostrec["uuid"]
                hostInfo["name"] = hostrec["name_label"]
                hostList.append(hostInfo)
        except Failure as error:
            return str(error), 400
        return json.dumps(hostList)


@app.route("/mobilexencenter/virtualmachines", methods=['POST'])
def getVMList():
    if request.method == 'POST':
        virtualMachines = []
        try:
            session = getServerSession()
            vmRecs= session.xenapi.VM.get_all_records()
            for vm in vmRecs :
                record = vmRecs[vm]
                if not(record["is_a_template"]) and not(record["is_control_domain"]):
                    vmInfo = dict()
                    vmInfo["name"] = record["name_label"]
                    vmInfo["uid"] = record["uuid"]
                    vmInfo['state'] = record["power_state"]
                    virtualMachines.append(vmInfo)
        except Failure as error:
            return str(error), 400
        return json.dumps(virtualMachines)
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/generalInfo", methods=['POST'])
def getVMInfo(vm_uid):
    if request.method == 'POST':
        vmInfo = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            vmrec = session.xenapi.VM.get_record(vmref)
            vmInfo["name"] = vmrec["name_label"]
            vmInfo['description'] = vmrec["name_description"]
            vmInfo["memory"] = vmrec["memory_target"]
            vmInfo['vcpuCount'] = vmrec["VCPUs_max"]
        except Failure as error:
            return str(error), 400
        return json.dumps(vmInfo)
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/generalInfo/update", methods=['POST'])
def setVMInfo(vm_uid):
    if request.method == 'POST':
        result = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            session.xenapi.VM.set_name_label(vmref, request.form.get('name',''))
            session.xenapi.VM.set_name_description(vmref, request.form.get('description',''))
            result['status'] = "success"
        except Failure as error:
            return str(error), 400
        return json.dumps(result)
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/start", methods=['POST'])
def startVm(vm_uid):
    if request.method == 'POST':
        result = dict()      
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            session.xenapi.Async.VM.start(vmref, False, True)
            task = session.xenapi.Async.VM.start(vmref, False, True)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info']
#            result['host'] = session.xenapi.host.get_name_label(task_record['resident_on'])
        except Failure as error:
            return str(error), 400
        return json.dumps(result)    
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/stop", methods=['POST'])
def stopVm(vm_uid):
    if request.method == 'POST':
        result = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.clean_shutdown(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info']
        except Failure as error:
            return str(error), 400
        return json.dumps(result)  
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/resume", methods=['POST'])
def resumeVm(vm_uid):
    if request.method == 'POST':
        result = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.resume(vmref, False, True)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info']
        except Failure as error:
            return str(error), 400
        return json.dumps(result)  
 
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/pause", methods=['POST'])
def pause(vm_uid):
    if request.method == 'POST':
        result = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.pause(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info']
        except Failure as error:
            return str(error), 400
        return json.dumps(result) 
   
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/unpause", methods=['POST'])
def unpause(vm_uid):
    if request.method == 'POST':
        result = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.unpause(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info']
        except Failure as error:
            return str(error), 400
        return json.dumps(result) 
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/suspend", methods=['POST'])
def suspend(vm_uid):
    if request.method == 'POST':
        result = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            task = session.xenapi.Async.VM.suspend(vmref)
            task_record = session.xenapi.task.get_record(task)
            while session.xenapi.task.get_status(task) == "pending": time.sleep(1)
            task_record = session.xenapi.task.get_record(task)
            result['status'] = task_record['status']
            if task_record['status'] == "failure" :
                result['errorInfo'] = task_record['error_info']
        except Failure as error:
            return str(error), 400
        return json.dumps(result) 
    
@app.route("/mobilexencenter/virtualmachines/<vm_uid>/memory", methods=['POST'])
def getVmMemoryConfig(vm_uid):
    if request.method == 'POST':
        memInfo = dict()
        try :
            session = getServerSession()    
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)         
            vmrec = session.xenapi.VM.get_record(vmref)
            memInfo['staticMax'] = int(vmrec["memory_static_max"])/1024/1024
            memInfo['staticMin'] = int(vmrec["memory_static_min"])/1024/1024
            memInfo['dynamicMax'] = int(vmrec["memory_dynamic_max"])/1024/1024
            memInfo['dynamicMin'] = int(vmrec["memory_dynamic_min"])/1024/1024            
        except Failure as error:
            return str(error), 400
        return json.dumps(memInfo)

@app.route("/mobilexencenter/virtualmachines/<vm_uid>/memory/update", methods=['POST'])
def updateVmMemoryConfig(vm_uid):
    if request.method == 'POST':
        result = dict()
        try :
            session = getServerSession()
            vmref = session.xenapi.VM.get_by_uuid(vm_uid)
            session.xenapi.VM.set_memory_limits(vmref, str(int(request.form['staticMin'])*1024*1024), str(int(request.form['staticMax'])*1024*1024), str(int(request.form['dynamicMin'])*1024*1024), str(int(request.form['dynamicMax'])*1024*1024))
            result['status'] = "success"
        except Failure as error:
            result['status'] = "failure"
            result['errorInfo'] = str(error)
            return json.dumps(result), 400
        return json.dumps(result)
    
@app.route("/mobilexencenter/virtualmachines/templates", methods=['POST'])
def getTemplates():
    if request.method == 'POST':
        templates = []
        try:
            session = getServerSession()    
            vmrecords = session.xenapi.VM.get_all_records()
            for vm in vmrecords: 
                record = vmrecords[vm]
                if record["is_a_template"]:
                    templateInfo = dict()
                    templateInfo['uuid'] = record['uuid']
                    templateInfo["name"] = record['name_label']
                    templates.append(templateInfo)
        except Failure as error:
            return str(error), 400
        return json.dumps(templates)
    
@app.route("/mobilexencenter/virtualmachines/<vmId>/clone", methods=['POST'])
def cloneVm(vmId):
    if request.method == 'POST':
        result = dict()
        try:
            session = getServerSession()    
            template = session.xenapi.VM.get_by_uuid(vmId)
            newVmName = request.form.get('newvmname', 'newVm')
            vm = session.xenapi.VM.clone(template, newVmName)
            pifs = session.xenapi.PIF.get_all_records()
            lowest = None
            for pifRef in pifs.keys():
                if (lowest is None) or (pifs[pifRef]['device'] < pifs[lowest]['device']):
                    lowest = pifRef
            network = session.xenapi.PIF.get_network(lowest)
            vif = { 'device': '0',
                    'network': network,
                    'VM': vm,
                    'MAC': "",
                    'MTU': "1500",
                    "qos_algorithm_type": "",
                    "qos_algorithm_params": {},
                    "other_config": {} }
            session.xenapi.VIF.create(vif)
            session.xenapi.VM.set_PV_args(vm, "noninteractive")
            pool = session.xenapi.pool.get_all()[0]
            default_sr = session.xenapi.pool.get_default_SR(pool)
            default_sr = session.xenapi.SR.get_record(default_sr)
            spec = provision.getProvisionSpec(session, vm)
            spec.setSR(default_sr['uuid'])
            provision.setProvisionSpec(session, vm, spec)
            session.xenapi.VM.provision(vm)    
            session.xenapi.VM.set_name_description(vm, request.form.get('description', ''))
            otherconfig = dict()
            otherconfig['install-repository'] = request.form.get('installurl', 'cdrom')
            session.xenapi.VM.set_other_config(vm, otherconfig)
            if request.form.has_key('initialmemory'):
                memValue = str(int(request.form['initialmemory'])*1024*1024)
                session.xenapi.VM.set_memory_limits(vm, memValue, memValue, memValue, memValue)
            if request.form.has_key('vcpus'):
                session.xenapi.VM.set_VCPUs_at_startup(vm, request.form['vcpus'])
#            session.xenapi.VM.start(vm, False, True)
            result['status'] = "success"
        except Failure as error:
            result['status'] = "failure"
            result['errorInfo'] = str(error)
            return json.dumps(result), 400
        return json.dumps(result)
    
@app.route("/mobilexencenter/virtualmachines/<vmId>/delete", methods=['POST'])
def deleteVM(vmId):
    if request.method == 'POST':
        result = dict()
        try:
            session = getServerSession()
            vm = session.xenapi.VM.get_by_uuid(vmId)    
            session.xenapi.VM.destroy(vm)
            result['status'] = "success"
        except Failure as error:
            result['status'] = "failure"
            result['errorInfo'] = str(error)
            return json.dumps(result), 400
        return json.dumps(result)
    
@app.route("/mobilexencenter/hosts/<hostId>/reboot", methods=['POST'])
def rebootHost(hostId):
    if request.method == 'POST':
        result = dict()
        try:
            session = getServerSession()
            host = session.xenapi.host.get_by_uuid(hostId)
            session.xenapi.host.disable(host) 
            session.xenapi.host.reboot(host)
            result['status'] = "success"
        except Failure as error:
            result['status'] = "failure"
            result['errorInfo'] = str(error)
            return json.dumps(result), 400
        return json.dumps(result)

@app.route("/mobilexencenter/hosts/<hostId>/shutdown", methods=['POST'])
def shutdownHost(hostId):
    if request.method == 'POST':
        result = dict()
        try:
            session = getServerSession()
            host = session.xenapi.host.get_by_uuid(hostId)
            session.xenapi.host.disable(host) 
            session.xenapi.host.shutdown(host)
            result['status'] = "success"
        except Failure as error:
            result['status'] = "failure"
            result['errorInfo'] = str(error)
            return json.dumps(result), 400
        return json.dumps(result)

if __name__ == "__main__":
    app.run(host = "0.0.0.0", port=8000)