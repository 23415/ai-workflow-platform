from fastapi import FastAPI
from pydantic import BaseModel
from pathlib import Path
import joblib
import os

app = FastAPI()

BASE_DIR = Path(__file__).resolve().parent

MODEL_PATH = os.getenv("AI_MODEL_PATH")

if not MODEL_PATH:
    raise RuntimeError("AI_MODEL_PATH environment variable not set")

model_path = Path(MODEL_PATH)

if not model_path.is_absolute():
    model_path = BASE_DIR / model_path

model = joblib.load(model_path)



@app.on_event("startup")
def load_model():
    global model
    model_path = Path(os.getenv("AI_MODEL_PATH", ""))
    if not model_path.exists():
        raise RuntimeError(f"Model not found at {model_path}")

    model = joblib.load(model_path)

class TicketRequest(BaseModel):
    title: str
    description: str

class TicketPrediction(BaseModel):
    category: str
    confidence: float

@app.get("/health")
def health():
    return {
        "status": "UP",
        "model_loaded": model is not None
    }



@app.post("/predict", response_model=TicketPrediction)
def predict(ticket: TicketRequest):
    if model is None:
            raise HTTPException(status_code=503, detail="Model not loaded")

    text = f"{ticket.title} {ticket.description}"

    probs = model.predict_proba([text])[0]
    classes = model.classes_

    best_idx = probs.argmax()

    return {
        "category": classes[best_idx],
        "confidence": float(probs[best_idx])
    }
