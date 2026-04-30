const API_BASE = 'http://localhost:8080/api';

const api = {
    getAuthToken() {
        return localStorage.getItem('token');
    },
    
    async request(endpoint, options = {}) {
        const url = API_BASE + endpoint;
        const authToken = this.getAuthToken();
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };
        
        if (authToken) {
            headers['Authorization'] = `Bearer ${authToken}`;
        }
        
        try {
            const response = await fetch(url, {
                ...options,
                headers
            });
            
            if (!response.ok) {
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    const error = await response.json();
                    throw new Error(error.message || 'Request failed');
                }
                throw new Error(`Request failed: ${response.status} ${response.statusText}`);
            }
            
            const text = await response.text();
            return text ? JSON.parse(text) : null;
        } catch (error) {
            console.error('API Error:', error);
            throw error;
        }
    },
    
    get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    },
    
    post(endpoint, data) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    
    put(endpoint, data) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    
    patch(endpoint, data) {
        return this.request(endpoint, {
            method: 'PATCH',
            body: JSON.stringify(data)
        });
    },
    
    delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    }
};

const AuthAPI = {
    async login(email, password) {
        const response = await api.post('/auth/login', { email, password });
        authToken = response.token;
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response));
        return response;
    },
    
    async register(name, email, password, role) {
        const response = await api.post('/auth/register', { name, email, password, role });
        authToken = response.token;
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response));
        return response;
    },
    
    logout() {
        authToken = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    },
    
    getCurrentUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    }
};

const ProjectAPI = {
    getAll() {
        return api.get('/projects');
    },
    
    getById(id) {
        return api.get(`/projects/${id}`);
    },
    
    create(data) {
        return api.post('/projects', data);
    },
    
    update(id, data) {
        return api.put(`/projects/${id}`, data);
    },
    
    delete(id) {
        return api.delete(`/projects/${id}`);
    }
};

const TaskAPI = {
    getAll() {
        return api.get('/tasks');
    },
    
    getById(id) {
        return api.get(`/tasks/${id}`);
    },
    
    create(data) {
        return api.post('/tasks', data);
    },
    
    update(id, data) {
        return api.put(`/tasks/${id}`, data);
    },
    
    updateStatus(id, status) {
        return api.patch(`/tasks/${id}/status`, { status });
    },
    
    delete(id) {
        return api.delete(`/tasks/${id}`);
    },
    
    getByProject(projectId) {
        return api.get(`/tasks/project/${projectId}`);
    },
    
    getByUser(userId) {
        return api.get(`/tasks/user/${userId}`);
    },
    
    getOverdue() {
        return api.get('/tasks/overdue');
    }
};

const WorkflowAPI = {
    getAll() {
        return api.get('/workflows');
    },
    
    create(data) {
        return api.post('/workflows', data);
    },
    
    update(id, data) {
        return api.put(`/workflows/${id}`, data);
    },
    
    delete(id) {
        return api.delete(`/workflows/${id}`);
    }
};

const NotificationAPI = {
    getByUser(userId) {
        return api.get(`/notifications/user/${userId}`);
    },
    
    getUnread(userId) {
        return api.get(`/notifications/user/${userId}/unread`);
    },
    
    markAsRead(id) {
        return api.patch(`/notifications/${id}/read`);
    },
    
    markAllAsRead(userId) {
        return api.patch(`/notifications/user/${userId}/read-all`);
    }
};

const DashboardAPI = {
    getStats() {
        return api.get('/dashboard');
    }
};

window.API = {
    auth: AuthAPI,
    projects: ProjectAPI,
    tasks: TaskAPI,
    workflows: WorkflowAPI,
    notifications: NotificationAPI,
    dashboard: DashboardAPI
};