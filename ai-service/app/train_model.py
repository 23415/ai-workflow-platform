import pandas as pd
import joblib

from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import Pipeline

# Load data
df = pd.read_csv("training_data.csv")

X = df["text"]
y = df["category"]

# Pipeline = vectorizer + model
pipeline = Pipeline([
    ("tfidf", TfidfVectorizer(
        ngram_range=(1,2),
        stop_words="english",
        max_features=5000
    )),
    ("clf", LogisticRegression(max_iter=1000))
])

# Train
pipeline.fit(X, y)

# Save model
joblib.dump(pipeline, "ticket_classifier.joblib")

print("✅ Model trained and saved")
