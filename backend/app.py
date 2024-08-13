#update this file for deployment 
from flask import Flask, send_from_directory
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
import os

app = Flask(__name__)
CORS(app)

app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///tasks.db"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False

db = SQLAlchemy(app)

# render.com

frontend_folder = os.path.abspath(os.path.join(os.getcwd(),"..","frontend","dist"))
dist_folder =  frontend_folder

#Server static files from "dist" folder under frontend directory 
@app.route("/",defaults={"filename":""})
@app.route("/<path:filename>")
def index(filename):
  if not filename:
    filename = "index.html"
  return send_from_directory(dist_folder,filename)

#api routes (create, delete and update tasks)
import routes
with app.app_context():
  db.create_all()

if __name__ == "__main__":
  port = int(os.environ.get("PORT", 5000))  # Use the port from the environment or default to 5000
  app.run(host='0.0.0.0', port=port, debug=False) 