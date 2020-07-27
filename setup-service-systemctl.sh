cp learn-backend/vidya-backend.service /etc/systemd/system
systemctl daemon-reload
systemctl enable vidya-backend.service