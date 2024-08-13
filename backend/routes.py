from app import app, db
from flask import request, jsonify
from models import Task


#CRUD operations 
# get all tasks 
@app.route("/api/tasks",methods=["GET"])
def get_tasks():
  tasks = Task.query.all()
  result = [task.to_json() for task in tasks]
  return jsonify(result)

# create tasks
@app.route("/api/tasks",methods=["POST"])
def create_task():
  try:
    data= request.json

    required_fields = ["name", "description"]
    for field in required_fields:
      if field not in data or not data.get(field):
        return jsonify({"error":f'Missing required field: {field}'}), 400

    name = data.get("name")
    description = data.get("description")

    new_task = Task(name=name, description=description)
    db.session.add(new_task)

    db.session.commit()
    return jsonify(new_task.to_json()),201
  except Exception as e:
    db.session.rollback()
    return jsonify({"error":str(e)}), 500

# delete friend
@app.route("/api/tasks/<int:id>",methods=["DELETE"])
def delete_task(id):
  try:
    task = Task.query.get(id)
    if task is None:
      return jsonify({"error":"task not found"}), 404
    
    db.session.delete(task)
    db.session.commit()
    return jsonify({"message":"Task deleted"}),200
  except Exception as e:
    db.session.rollback()
    return jsonify({"error": str(e)}), 500
    

# update task 
@app.route("/api/tasks/<int:id>",methods=["PATCH"])
def update_task(id):
  try:
   task = Task.query.get(id)
   if task is None:
     return jsonify({"error":"task not found"}), 404
   
   data = request.json

   task.name = data.get("name", task.name)
   task.description = data.get("description", task.description)

   db.session.commit()
   return jsonify(task.to_json()), 200
   
  except Exception as e:
    db.session.rollback()
    return jsonify({"error": str(e)}), 500

