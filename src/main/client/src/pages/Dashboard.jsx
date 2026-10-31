import { useState, useEffect, useContext } from 'react';
import { UserContext } from './UserContext'
import api from '../axios';

function Dashboard() {
    const [files, setFiles] = useState([]);
    const [selectedFile, setSelectedFile] = useState(null);
    const { user } = useContext(UserContext);
    // Fetch the list of uploaded files from the backend. It sends a GET request to the '/files' endpoint, including the authorization token in the headers. If successful, it updates the 'files' state with the retrieved data.
    async function fetchFiles() {
            try {
                // Send a GET request to the backend to fetch the list of files, including the authorization token in the headers.
                const response = await api.get('/files', { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
                console.log('Fetched files:', response.data);
                if(response.status === 200) {
                    setFiles(response.data);
                }
            } catch (error) {
                console.error('Failed to fetch files:', error);
            }
    }

    // Handle the file upload process. If a file is selected, it creates a FormData object and sends it to the backend via a POST request.
    async function handleUpload() {
        if(!selectedFile) {
            console.error('No file selected for upload.');
            return;
        } else{
            const formData = new FormData();
            formData.append('file', selectedFile); 
            try {
                const response = await api.post('/files', formData, { headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` } });
                if(response.status === 200) {
                    console.log('File uploaed successfully.');
                    fetchFiles(); // Refresh the list of files after a successful upload.
                }
            } catch (error) {
                console.error('Failed to upload file:', error);
            }
        }
    }

    // Fetches the file everytime the /dashboard page is first built.
    useEffect(() => {
        fetchFiles();   
    }, []);

    return (
        <div className="min-h-screen bg-gray-100 px-4 py-10">
            <div>
                <h1>Hello {user}</h1>
            </div>
        <div className="mx-auto max-w-2xl bg-white rounded-lg shadow-md p-8 space-y-6">
            <h1 className="text-2xl font-semibold text-gray-800">Dashboard</h1>

            <div className="space-y-2">
                <h2 className="text-lg font-medium text-gray-700">Uploaded Files</h2>

                {/* // Display the list of uploaded files. Each file is represented as a list item. */}
                <ul className="divide-y divide-gray-200 rounded-md border border-gray-200">
                    {files.map((file) => (
                        <li key={file.id} className="flex items-center justify-between px-4 py-3">
                            <span className="truncate text-gray-800">{file.name}</span>
                        </li>
                    ))}
                </ul>
            </div>

            <div className="space-y-2">
                <label htmlFor="file-upload" className="block text-sm font-medium text-gray-700">Upload a file</label>
                <input type="file" id="file-upload" onChange={(e) => setSelectedFile(e.target.files[0])}
                    className="block w-full text-sm text-gray-700 file:mr-4 file:rounded-md file:border-0 file:bg-gray-100 file:px-4 file:py-2 hover:file:bg-gray-200 file:cursor-pointer"/>
                <button onClick={handleUpload} className="w-full rounded-md bg-blue-600 py-2 font-medium text-white hover:bg-blue-700 cursor-pointer">Upload</button>
            </div>
        </div>
        </div>
        
    )
}

export default Dashboard;
        