import React from 'react';
import { AlertCircle } from 'lucide-react';

interface ErrorMessageProps {
    message: string;
}

const ErrorMessage: React.FC<ErrorMessageProps> = ({ message }) => {
    return (
        <div className="bg-red-900/20 border border-red-500/50 rounded-lg p-4 flex items-start space-x-3">
            <AlertCircle className="w-5 h-5 text-red-500 flex-shrink-0 mt-0.5" />
            <div>
                <h3 className="font-semibold text-red-400 mb-1">Erreur</h3>
                <p className="text-red-300 text-sm">{message}</p>
            </div>
        </div>
    );
};

export default ErrorMessage;
