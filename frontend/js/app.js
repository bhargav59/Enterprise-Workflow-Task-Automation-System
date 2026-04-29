let currentUser = null;

document.addEventListener('DOMContentLoaded', () => {
    init();
});

async function init() {
    currentUser = API.auth.getCurrentUser();
    
    if (currentUser) {
        showAuthenticatedUI();
    } else {
        showAuthPage();
    }
    
    setupEventListeners();
}

function setupEventListeners() {
    document.getElementById('logoutBtn')?.addEventListener('click', handleLogout);
    
    document.querySelectorAll('.auth-tab').forEach(tab => {
        tab.addEventListener('click', () => switchAuthTab(tab.dataset.tab));
    });
    
    document.getElementById('loginBtnSubmit')?.addEventListener('click', handleLogin);
    document.getElementById('registerBtnSubmit')?.addEventListener('click', handleRegister);
    
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            navigateTo(link.dataset.page);
        });
    });
    
    document.getElementById('newProjectBtn')?.addEventListener('click', showNewProjectModal);
    document.getElementById('newTaskBtn')?.addEventListener('click', showNewTaskModal);
    document.getElementById('newWorkflowBtn')?.addEventListener('click', showNewWorkflowModal);
    document.getElementById('markAllReadBtn')?.addEventListener('click', handleMarkAllRead);
    
    document.querySelector('.close')?.addEventListener('click', closeModal);
    document.getElementById('modal')?.addEventListener('click', (e) => {
        if (e.target.id === 'modal') closeModal();
    });
}

function showAuthPage() {
    document.getElementById('authPage').classList.remove('hidden');
    document.getElementById('navbar').classList.add('hidden');
    hideAllPages();
    document.getElementById('authPage').classList.remove('hidden');
}

function showAuthenticatedUI() {
    document.getElementById('authPage').classList.add('hidden');
    document.getElementById('navbar').classList.remove('hidden');
    document.getElementById('userName').textContent = currentUser?.name || 'User';
    navigateTo('dashboard');
}

function hideAllPages() {
    document.querySelectorAll('.page').forEach(page => page.classList.add('hidden'));
}

function navigateTo(page) {
    hideAllPages();
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.classList.remove('active');
        if (link.dataset.page === page) link.classList.add('active');
    });
    
    const pageElement = document.getElementById(`${page}Page`);
    if (pageElement) {
        pageElement.classList.remove('hidden');
    }
    
    switch (page) {
        case 'dashboard': loadDashboard(); break;
        case 'projects': loadProjects(); break;
        case 'tasks': loadTasks(); break;
        case 'kanban': loadKanban(); break;
        case 'workflows': loadWorkflows(); break;
        case 'notifications': loadNotifications(); break;
    }
}

function switchAuthTab(tab) {
    document.querySelectorAll('.auth-tab').forEach(t => t.classList.remove('active'));
    document.querySelector(`.auth-tab[data-tab="${tab}"]`).classList.add('active');
    
    if (tab === 'login') {
        document.getElementById('loginForm').classList.remove('hidden');
        document.getElementById('registerForm').classList.add('hidden');
    } else {
        document.getElementById('loginForm').classList.add('hidden');
        document.getElementById('registerForm').classList.remove('hidden');
    }
}

async function handleLogin() {
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    
    try {
        currentUser = await API.auth.login(email, password);
        showAuthenticatedUI();
    } catch (error) {
        alert('Login failed: ' + error.message);
    }
}

async function handleRegister() {
    const name = document.getElementById('registerName').value;
    const email = document.getElementById('registerEmail').value;
    const password = document.getElementById('registerPassword').value;
    const role = document.getElementById('registerRole').value;
    
    try {
        currentUser = await API.auth.register(name, email, password, role);
        showAuthenticatedUI();
    } catch (error) {
        alert('Registration failed: ' + error.message);
    }
}

function handleLogout() {
    API.auth.logout();
    currentUser = null;
    showAuthPage();
}

async function loadDashboard() {
    try {
        const stats = await API.dashboard.getStats();
        document.getElementById('statProjects').textContent = stats.totalProjects;
        document.getElementById('statTasks').textContent = stats.totalTasks;
        document.getElementById('statCompleted').textContent = stats.completedTasks;
        document.getElementById('statInProgress').textContent = stats.inProgressTasks;
        document.getElementById('statPending').textContent = stats.pendingTasks;
        document.getElementById('statOverdue').textContent = stats.overdueTasks;
    } catch (error) {
        console.error('Failed to load dashboard:', error);
    }
}

async function loadProjects() {
    try {
        const projects = await API.projects.getAll();
        const container = document.getElementById('projectsList');
        container.innerHTML = projects.map(project => `
            <div class="card">
                <h3>${project.name}</h3>
                <p>${project.description || 'No description'}</p>
                <div class="actions">
                    <button class="edit-btn" onclick="editProject(${project.id})">Edit</button>
                    <button class="delete-btn" onclick="deleteProject(${project.id})">Delete</button>
                </div>
            </div>
        `).join('');
    } catch (error) {
        console.error('Failed to load projects:', error);
    }
}

async function loadTasks() {
    try {
        const tasks = await API.tasks.getAll();
        const projects = await API.projects.getAll();
        
        const container = document.getElementById('tasksList');
        container.innerHTML = tasks.map(task => `
            <div class="task-item">
                <div class="task-info">
                    <h4>${task.title}</h4>
                    <span>${task.description || ''} | ${task.project?.name || 'No project'}</span>
                </div>
                <span class="task-status ${task.status}">${task.status}</span>
            </div>
        `).join('');
    } catch (error) {
        console.error('Failed to load tasks:', error);
    }
}

async function loadKanban() {
    try {
        const tasks = await API.tasks.getAll();
        
        const columns = {
            TODO: [],
            IN_PROGRESS: [],
            DONE: []
        };
        
        tasks.forEach(task => {
            if (columns[task.status]) {
                columns[task.status].push(task);
            }
        });
        
        Object.keys(columns).forEach(status => {
            const container = document.querySelector(`.kanban-tasks[data-status="${status}"]`);
            container.innerHTML = columns[status].map(task => `
                <div class="kanban-task" draggable="true" data-id="${task.id}">
                    <h4>${task.title}</h4>
                    <p>${task.description || ''}</p>
                </div>
            `).join('');
            
            setupDragDrop(container, status);
        });
    } catch (error) {
        console.error('Failed to load kanban:', error);
    }
}

function setupDragDrop(container, targetStatus) {
    let draggedItem = null;
    
    container.addEventListener('dragstart', (e) => {
        if (e.target.classList.contains('kanban-task')) {
            draggedItem = e.target;
            e.target.classList.add('dragging');
        }
    });
    
    container.addEventListener('dragend', (e) => {
        if (e.target.classList.contains('kanban-task')) {
            e.target.classList.remove('dragging');
        }
    });
    
    container.addEventListener('dragover', (e) => {
        e.preventDefault();
    });
    
    container.addEventListener('drop', async (e) => {
        e.preventDefault();
        const taskId = draggedItem?.dataset.id;
        if (taskId && targetStatus) {
            try {
                await API.tasks.updateStatus(parseInt(taskId), targetStatus);
                loadKanban();
            } catch (error) {
                console.error('Failed to update task status:', error);
            }
        }
    });
}

async function loadWorkflows() {
    try {
        const workflows = await API.workflows.getAll();
        const container = document.getElementById('workflowsList');
        
        container.innerHTML = workflows.map(workflow => `
            <div class="workflow-item">
                <div class="workflow-info">
                    <h4>${workflow.name}</h4>
                    <code>IF ${workflow.condition} THEN ${workflow.action}</code>
                </div>
                <label class="workflow-toggle">
                    <input type="checkbox" ${workflow.enabled ? 'checked' : ''} 
                           onchange="toggleWorkflow(${workflow.id}, this.checked)">
                    <span class="slider"></span>
                </label>
            </div>
        `).join('');
    } catch (error) {
        console.error('Failed to load workflows:', error);
    }
}

async function loadNotifications() {
    if (!currentUser) return;
    
    try {
        const notifications = await API.notifications.getByUser(currentUser.id);
        const container = document.getElementById('notificationsList');
        
        container.innerHTML = notifications.map(notification => `
            <div class="notification-item ${notification.readStatus ? '' : 'unread'}">
                <p>${notification.message}</p>
                <span class="notification-time">${new Date(notification.createdAt).toLocaleString()}</span>
            </div>
        `).join('');
    } catch (error) {
        console.error('Failed to load notifications:', error);
    }
}

function showNewProjectModal() {
    const modalBody = document.getElementById('modalBody');
    modalBody.innerHTML = `
        <h2>New Project</h2>
        <form class="modal-form" onsubmit="submitProject(event)">
            <input type="text" id="projectName" placeholder="Project Name" required>
            <textarea id="projectDescription" placeholder="Description"></textarea>
            <button type="submit">Create Project</button>
        </form>
    `;
    document.getElementById('modal').classList.remove('hidden');
}

async function submitProject(e) {
    e.preventDefault();
    
    const data = {
        name: document.getElementById('projectName').value,
        description: document.getElementById('projectDescription').value
    };
    
    try {
        await API.projects.create(data);
        closeModal();
        loadProjects();
    } catch (error) {
        alert('Failed to create project: ' + error.message);
    }
}

async function editProject(id) {
    try {
        const project = await API.projects.getById(id);
        const modalBody = document.getElementById('modalBody');
        
        modalBody.innerHTML = `
            <h2>Edit Project</h2>
            <form class="modal-form" onsubmit="updateProject(event, ${id})">
                <input type="text" id="projectName" value="${project.name}" required>
                <textarea id="projectDescription">${project.description || ''}</textarea>
                <button type="submit">Update Project</button>
            </form>
        `;
        document.getElementById('modal').classList.remove('hidden');
    } catch (error) {
        console.error('Failed to load project:', error);
    }
}

async function updateProject(e, id) {
    e.preventDefault();
    
    const data = {
        name: document.getElementById('projectName').value,
        description: document.getElementById('projectDescription').value
    };
    
    try {
        await API.projects.update(id, data);
        closeModal();
        loadProjects();
    } catch (error) {
        alert('Failed to update project: ' + error.message);
    }
}

async function deleteProject(id) {
    if (!confirm('Are you sure you want to delete this project?')) return;
    
    try {
        await API.projects.delete(id);
        loadProjects();
    } catch (error) {
        alert('Failed to delete project: ' + error.message);
    }
}

async function showNewTaskModal() {
    try {
        const projects = await API.projects.getAll();
        const modalBody = document.getElementById('modalBody');
        
        modalBody.innerHTML = `
            <h2>New Task</h2>
            <form class="modal-form" onsubmit="submitTask(event)">
                <input type="text" id="taskTitle" placeholder="Task Title" required>
                <textarea id="taskDescription" placeholder="Description"></textarea>
                <input type="date" id="taskDeadline" placeholder="Deadline">
                <select id="taskProject">
                    <option value="">Select Project</option>
                    ${projects.map(p => `<option value="${p.id}">${p.name}</option>`).join('')}
                </select>
                <button type="submit">Create Task</button>
            </form>
        `;
        document.getElementById('modal').classList.remove('hidden');
    } catch (error) {
        console.error('Failed to load projects:', error);
    }
}

async function submitTask(e) {
    e.preventDefault();
    
    const data = {
        title: document.getElementById('taskTitle').value,
        description: document.getElementById('taskDescription').value,
        deadline: document.getElementById('taskDeadline').value,
        projectId: document.getElementById('taskProject').value ? parseInt(document.getElementById('taskProject').value) : null
    };
    
    try {
        await API.tasks.create(data);
        closeModal();
        loadTasks();
    } catch (error) {
        alert('Failed to create task: ' + error.message);
    }
}

function showNewWorkflowModal() {
    const modalBody = document.getElementById('modalBody');
    modalBody.innerHTML = `
        <h2>New Workflow</h2>
        <form class="modal-form" onsubmit="submitWorkflow(event)">
            <input type="text" id="workflowName" placeholder="Workflow Name" required>
            <input type="text" id="workflowCondition" placeholder="Condition (e.g., STATUS=DONE)" required>
            <input type="text" id="workflowAction" placeholder="Action (e.g., NOTIFY)" required>
            <button type="submit">Create Workflow</button>
        </form>
    `;
    document.getElementById('modal').classList.remove('hidden');
}

async function submitWorkflow(e) {
    e.preventDefault();
    
    const data = {
        name: document.getElementById('workflowName').value,
        condition: document.getElementById('workflowCondition').value,
        action: document.getElementById('workflowAction').value
    };
    
    try {
        await API.workflows.create(data);
        closeModal();
        loadWorkflows();
    } catch (error) {
        alert('Failed to create workflow: ' + error.message);
    }
}

async function toggleWorkflow(id, enabled) {
    try {
        const workflows = await API.workflows.getAll();
        const workflow = workflows.find(w => w.id === id);
        if (workflow) {
            workflow.enabled = enabled;
            await API.workflows.update(id, workflow);
        }
    } catch (error) {
        console.error('Failed to toggle workflow:', error);
    }
}

async function handleMarkAllRead() {
    if (!currentUser) return;
    
    try {
        await API.notifications.markAllAsRead(currentUser.id);
        loadNotifications();
    } catch (error) {
        console.error('Failed to mark notifications as read:', error);
    }
}

function closeModal() {
    document.getElementById('modal').classList.add('hidden');
}

window.editProject = editProject;
window.deleteProject = deleteProject;
window.updateProject = updateProject;
window.submitProject = submitProject;
window.submitTask = submitTask;
window.submitWorkflow = submitWorkflow;
window.toggleWorkflow = toggleWorkflow;