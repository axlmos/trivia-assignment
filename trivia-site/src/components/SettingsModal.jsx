import { useState } from 'react';
import { CATEGORIES } from '../constants';

export default function SettingsModal({ currentSettings, onSave, onClose }) {
    const [tempSettings, setTempSettings] = useState(currentSettings);

    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <h3>Game Settings</h3>

                <div className="form-group">
                    <label className="label">Amount of Questions (1-50):</label>
                    <input
                        type="number"
                        min="1" max="50"
                        value={tempSettings.amount}
                        onChange={(e) => setTempSettings({ ...tempSettings, amount: e.target.value })}
                        className="input"
                    />
                </div>

                <div className="form-group">
                    <label className="label">Category:</label>
                    <select
                        value={tempSettings.category}
                        onChange={(e) => setTempSettings({ ...tempSettings, category: e.target.value })}
                        className="input"
                    >
                        {CATEGORIES.map((cat) => (
                            <option value={cat.id}>{cat.name}</option>
                        ))}
                    </select>
                </div>

                <div className="form-group">
                    <label className="label">Difficulty:</label>
                    <select
                        value={tempSettings.difficulty}
                        onChange={(e) => setTempSettings({ ...tempSettings, difficulty: e.target.value })}
                        className="input"
                    >
                        <option value="any">Any</option>
                        <option value="easy">Easy</option>
                        <option value="medium">Medium</option>
                        <option value="hard">Hard</option>
                    </select>
                </div>

                <div className="modal-actions">
                    <button onClick={() => onSave(tempSettings)} className="button">Apply & Restart</button>
                    <button onClick={onClose} className="cancel-button">Cancel</button>
                </div>
            </div>
        </div>
    );
}