from pydantic import BaseModel

class TicketRequest(BaseModel):
    ticketId: int
    title: str
    description: str

class PredictionResponse(BaseModel):
    ticketId: int
    label: str
    confidence: float
