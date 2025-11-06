/**
 * Student Management System - Custom JavaScript
 * Enhanced interactions and animations
 */

// Application namespace
const StudentApp = {
    // Configuration
    config: {
        animationDuration: 300,
        tableRowAnimationDelay: 50,
        notificationTimeout: 5000,
        autoSaveDelay: 2000
    },

    // Initialize the application
    init() {
        this.initializeComponents();
        this.bindEvents();
        this.startAnimations();
        this.initializeTheme();
        console.log('Student Management System initialized successfully');
    },

    // Initialize components
    initializeComponents() {
        this.initializeTables();
        this.initializeForms();
        this.initializeStats();
        this.initializeNavigation();
        this.initializeModals();
    },

    // Initialize enhanced tables
    initializeTables() {
        const tables = document.querySelectorAll('.table-modern');
        tables.forEach(table => {
            this.enhanceTable(table);
        });
    },

    // Enhance table functionality
    enhanceTable(table) {
        const rows = table.querySelectorAll('tbody tr');
        
        // Add progressive loading animation
        rows.forEach((row, index) => {
            row.style.opacity = '0';
            row.style.transform = 'translateY(20px)';
            
            setTimeout(() => {
                row.style.transition = 'all 0.5s ease';
                row.style.opacity = '1';
                row.style.transform = 'translateY(0)';
            }, index * this.config.tableRowAnimationDelay);
        });

        // Add row click handlers
        rows.forEach(row => {
            row.addEventListener('click', (e) => {
                this.handleRowSelection(row, e);
            });

            // Add keyboard navigation
            row.addEventListener('keydown', (e) => {
                if (e.key === 'Enter' || e.key === ' ') {
                    e.preventDefault();
                    this.handleRowSelection(row, e);
                }
            });
        });

        // Add sorting functionality
        this.addTableSorting(table);
    },

    // Handle row selection with enhanced visual feedback
    handleRowSelection(row, event) {
        const table = row.closest('table');
        const radioInput = row.querySelector('input[type="radio"]');
        
        // Remove previous selection
        table.querySelectorAll('tbody tr').forEach(r => {
            r.classList.remove('selected');
        });

        // Add selection to current row
        row.classList.add('selected');
        
        if (radioInput) {
            radioInput.checked = true;
            
            // Trigger the existing selectStudent/selectBook function
            if (typeof selectStudent === 'function') {
                const cells = row.cells;
                const id = cells[1].textContent;
                const firstName = cells[2].textContent;
                const lastName = cells[3].textContent;
                const marks = cells[4].querySelector('.badge')?.textContent || cells[4].textContent;
                selectStudent(id, firstName, lastName, marks);
            }
        }

        // Visual feedback
        this.addRippleEffect(row, event);
    },

    // Add ripple effect to elements
    addRippleEffect(element, event) {
        const ripple = document.createElement('span');
        const rect = element.getBoundingClientRect();
        const size = Math.max(rect.width, rect.height);
        const x = event.clientX - rect.left - size / 2;
        const y = event.clientY - rect.top - size / 2;

        ripple.style.width = ripple.style.height = size + 'px';
        ripple.style.left = x + 'px';
        ripple.style.top = y + 'px';
        ripple.classList.add('ripple');

        element.appendChild(ripple);

        setTimeout(() => {
            ripple.remove();
        }, 600);
    },

    // Add table sorting functionality
    addTableSorting(table) {
        const headers = table.querySelectorAll('thead th');
        headers.forEach((header, index) => {
            if (index === 0) return; // Skip checkbox column

            header.style.cursor = 'pointer';
            header.classList.add('sortable');
            
            header.addEventListener('click', () => {
                this.sortTable(table, index);
            });
        });
    },

    // Sort table by column
    sortTable(table, columnIndex) {
        const tbody = table.querySelector('tbody');
        const rows = Array.from(tbody.querySelectorAll('tr'));
        const isNumeric = this.isColumnNumeric(rows, columnIndex);
        
        // Determine sort direction
        const currentHeader = table.querySelectorAll('thead th')[columnIndex];
        const isAscending = !currentHeader.classList.contains('sort-desc');
        
        // Clear all sort indicators
        table.querySelectorAll('thead th').forEach(th => {
            th.classList.remove('sort-asc', 'sort-desc');
        });

        // Add sort indicator
        currentHeader.classList.add(isAscending ? 'sort-asc' : 'sort-desc');

        // Sort rows
        rows.sort((a, b) => {
            const aValue = a.cells[columnIndex].textContent.trim();
            const bValue = b.cells[columnIndex].textContent.trim();

            let comparison;
            if (isNumeric) {
                comparison = parseFloat(aValue) - parseFloat(bValue);
            } else {
                comparison = aValue.localeCompare(bValue);
            }

            return isAscending ? comparison : -comparison;
        });

        // Re-append sorted rows with animation
        rows.forEach((row, index) => {
            row.style.opacity = '0';
            setTimeout(() => {
                tbody.appendChild(row);
                row.style.opacity = '1';
            }, index * 20);
        });
    },

    // Check if column contains numeric data
    isColumnNumeric(rows, columnIndex) {
        const sampleValues = rows.slice(0, 3).map(row => 
            row.cells[columnIndex].textContent.trim()
        );
        return sampleValues.every(value => !isNaN(parseFloat(value)));
    },

    // Initialize enhanced forms
    initializeForms() {
        const forms = document.querySelectorAll('form');
        forms.forEach(form => {
            this.enhanceForm(form);
        });
    },

    // Enhance form functionality
    enhanceForm(form) {
        const inputs = form.querySelectorAll('input, select, textarea');
        
        inputs.forEach(input => {
            // Add floating label effect
            this.addFloatingLabel(input);
            
            // Add real-time validation
            this.addInputValidation(input);
            
            // Add auto-save functionality - DISABLED
            // this.addAutoSave(input);
        });

        // Add form submission enhancement
        form.addEventListener('submit', (e) => {
            this.handleFormSubmission(form, e);
        });
    },

    // Add floating label effect
    addFloatingLabel(input) {
        const wrapper = input.parentElement;
        if (!wrapper.classList.contains('form-floating')) return;

        input.addEventListener('focus', () => {
            wrapper.classList.add('focused');
        });

        input.addEventListener('blur', () => {
            if (!input.value) {
                wrapper.classList.remove('focused');
            }
        });

        // Check initial state
        if (input.value) {
            wrapper.classList.add('focused');
        }
    },

    // Add input validation
    addInputValidation(input) {
        if (!input.hasAttribute('required')) return;

        input.addEventListener('blur', () => {
            this.validateInput(input);
        });

        input.addEventListener('input', () => {
            if (input.classList.contains('is-invalid')) {
                this.validateInput(input);
            }
        });
    },

    // Validate single input
    validateInput(input) {
        const value = input.value.trim();
        let isValid = true;
        let message = '';

        // Required validation
        if (input.hasAttribute('required') && !value) {
            isValid = false;
            message = 'This field is required';
        }

        // Type-specific validation
        if (value && input.type === 'number') {
            const min = input.getAttribute('min');
            const max = input.getAttribute('max');
            const numValue = parseFloat(value);

            if (isNaN(numValue)) {
                isValid = false;
                message = 'Please enter a valid number';
            } else if (min !== null && numValue < parseFloat(min)) {
                isValid = false;
                message = `Value must be at least ${min}`;
            } else if (max !== null && numValue > parseFloat(max)) {
                isValid = false;
                message = `Value must be at most ${max}`;
            }
        }

        // Update UI
        input.classList.toggle('is-invalid', !isValid);
        input.classList.toggle('is-valid', isValid && value);

        // Show/hide error message
        this.updateErrorMessage(input, message);

        return isValid;
    },

    // Update error message
    updateErrorMessage(input, message) {
        let errorElement = input.parentElement.querySelector('.invalid-feedback');
        
        if (message) {
            if (!errorElement) {
                errorElement = document.createElement('div');
                errorElement.className = 'invalid-feedback';
                input.parentElement.appendChild(errorElement);
            }
            errorElement.textContent = message;
        } else if (errorElement) {
            errorElement.remove();
        }
    },

    // Add auto-save functionality - DISABLED
    addAutoSave(input) {
        // Auto-save disabled by user request
        return;
    },

    // Auto-save form data to localStorage - DISABLED
    autoSaveForm(form) {
        // Auto-save disabled by user request
        return;
    },

    // Handle form submission with enhancement
    handleFormSubmission(form, event) {
        // Validate all inputs
        const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
        let isFormValid = true;

        inputs.forEach(input => {
            if (!this.validateInput(input)) {
                isFormValid = false;
            }
        });

        if (!isFormValid) {
            event.preventDefault();
            this.showNotification('Please fix the errors in the form', 'error');
            return;
        }

        // Add loading state
        const submitButton = form.querySelector('button[type="submit"]');
        if (submitButton) {
            this.setButtonLoading(submitButton, true);
        }

        // Clear auto-save data on successful submission
        if (form.id) {
            localStorage.removeItem(`form_${form.id}_autosave`);
        }
    },

    // Set button loading state
    setButtonLoading(button, isLoading) {
        if (isLoading) {
            button.dataset.originalText = button.innerHTML;
            button.innerHTML = '<span class="loading-spinner"></span> Processing...';
            button.disabled = true;
        } else {
            button.innerHTML = button.dataset.originalText || button.innerHTML;
            button.disabled = false;
        }
    },

    // Initialize statistics with animations
    initializeStats() {
        const statsCards = document.querySelectorAll('.stats-card, .stats-card-enhanced');
        
        statsCards.forEach((card, index) => {
            // Animate numbers
            const numberElement = card.querySelector('.stats-number, h3');
            if (numberElement) {
                this.animateNumber(numberElement, index * 200);
            }
        });
    },

    // Animate number counting
    animateNumber(element, delay = 0) {
        const finalValue = parseInt(element.textContent) || 0;
        const duration = 1500;
        const increment = finalValue / (duration / 16);
        let currentValue = 0;

        setTimeout(() => {
            const timer = setInterval(() => {
                currentValue += increment;
                if (currentValue >= finalValue) {
                    currentValue = finalValue;
                    clearInterval(timer);
                }
                element.textContent = Math.floor(currentValue);
            }, 16);
        }, delay);
    },

    // Initialize navigation enhancements
    initializeNavigation() {
        const navLinks = document.querySelectorAll('.navigation-links a, .nav-enhanced a');
        
        navLinks.forEach(link => {
            link.addEventListener('click', (e) => {
                this.handleNavigation(link, e);
            });
        });

        // Add breadcrumb functionality
        this.updateBreadcrumbs();
    },

    // Handle navigation with smooth transitions
    handleNavigation(link, event) {
        const href = link.getAttribute('href');
        
        if (href && href.startsWith('/')) {
            // Add loading indicator
            this.showPageLoading();
            
            // Optional: Add smooth page transition
            document.body.style.opacity = '0.7';
            setTimeout(() => {
                window.location.href = href;
            }, 150);
        }
    },

    // Show page loading indicator
    showPageLoading() {
        const loadingOverlay = document.createElement('div');
        loadingOverlay.className = 'loading-overlay';
        loadingOverlay.innerHTML = '<div class="loading-spinner"></div>';
        document.body.appendChild(loadingOverlay);
    },

    // Update breadcrumbs based on current page
    updateBreadcrumbs() {
        const currentPath = window.location.pathname;
        const breadcrumbContainer = document.querySelector('.breadcrumb');
        
        if (!breadcrumbContainer) return;

        // Clear existing breadcrumbs
        breadcrumbContainer.innerHTML = '';

        // Add home
        this.addBreadcrumbItem(breadcrumbContainer, 'Home', '/', currentPath === '/');

        // Add current page
        if (currentPath === '/books') {
            this.addBreadcrumbItem(breadcrumbContainer, 'Manage Books', '/books', true);
        }
    },

    // Add breadcrumb item
    addBreadcrumbItem(container, text, href, isActive) {
        const item = document.createElement('li');
        item.className = `breadcrumb-item ${isActive ? 'active' : ''}`;
        
        if (isActive) {
            item.textContent = text;
        } else {
            const link = document.createElement('a');
            link.href = href;
            link.textContent = text;
            item.appendChild(link);
        }
        
        container.appendChild(item);
    },

    // Initialize modal functionality
    initializeModals() {
        // Add modal triggers
        document.addEventListener('click', (e) => {
            if (e.target.matches('[data-modal]')) {
                const modalId = e.target.getAttribute('data-modal');
                this.showModal(modalId);
            }
        });

        // Add keyboard handlers for modals
        document.addEventListener('keydown', (e) => {
            if (e.key === 'Escape') {
                this.hideActiveModal();
            }
        });
    },

    // Show modal
    showModal(modalId) {
        const modal = document.getElementById(modalId);
        if (!modal) return;

        modal.style.display = 'block';
        setTimeout(() => {
            modal.classList.add('show');
        }, 10);

        // Add backdrop click handler
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                this.hideModal(modal);
            }
        });
    },

    // Hide modal
    hideModal(modal) {
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300);
    },

    // Hide active modal
    hideActiveModal() {
        const activeModal = document.querySelector('.modal.show');
        if (activeModal) {
            this.hideModal(activeModal);
        }
    },

    // Initialize theme system
    initializeTheme() {
        // Check for saved theme
        const savedTheme = localStorage.getItem('theme') || 'light';
        this.setTheme(savedTheme);

        // Add theme toggle if exists
        const themeToggle = document.querySelector('[data-theme-toggle]');
        if (themeToggle) {
            themeToggle.addEventListener('click', () => {
                this.toggleTheme();
            });
        }
    },

    // Set theme
    setTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem('theme', theme);
    },

    // Toggle theme
    toggleTheme() {
        const currentTheme = document.documentElement.getAttribute('data-theme') || 'light';
        const newTheme = currentTheme === 'light' ? 'dark' : 'light';
        this.setTheme(newTheme);
    },

    // Start animations
    startAnimations() {
        // Animate elements on scroll
        this.initializeScrollAnimations();
        
        // Start periodic animations
        this.startPeriodicAnimations();
    },

    // Initialize scroll-based animations
    initializeScrollAnimations() {
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('animate-in');
                }
            });
        }, observerOptions);

        // Observe elements
        document.querySelectorAll('.card-enhanced, .stats-card-enhanced, .form-modern').forEach(el => {
            observer.observe(el);
        });
    },

    // Start periodic animations
    startPeriodicAnimations() {
        // Subtle background animation
        setInterval(() => {
            const elements = document.querySelectorAll('.header-section, .stats-card-enhanced');
            elements.forEach(el => {
                el.style.transform = 'scale(1.01)';
                setTimeout(() => {
                    el.style.transform = 'scale(1)';
                }, 200);
            });
        }, 30000);
    },

    // Show notification
    showNotification(message, type = 'info', duration = this.config.notificationTimeout) {
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        notification.innerHTML = `
            <i class="fas fa-${this.getNotificationIcon(type)}"></i>
            <span>${message}</span>
            <button class="notification-close">&times;</button>
        `;

        // Add to container or create one
        let container = document.querySelector('.notification-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'notification-container';
            document.body.appendChild(container);
        }

        container.appendChild(notification);

        // Add close handler
        notification.querySelector('.notification-close').addEventListener('click', () => {
            this.hideNotification(notification);
        });

        // Auto-hide
        setTimeout(() => {
            this.hideNotification(notification);
        }, duration);

        // Animate in
        setTimeout(() => {
            notification.classList.add('show');
        }, 10);
    },

    // Get notification icon
    getNotificationIcon(type) {
        const icons = {
            info: 'info-circle',
            success: 'check-circle',
            warning: 'exclamation-triangle',
            error: 'times-circle'
        };
        return icons[type] || icons.info;
    },

    // Hide notification
    hideNotification(notification) {
        notification.classList.remove('show');
        setTimeout(() => {
            notification.remove();
        }, 300);
    },

    // Utility functions
    utils: {
        // Debounce function
        debounce(func, wait) {
            let timeout;
            return function executedFunction(...args) {
                const later = () => {
                    clearTimeout(timeout);
                    func(...args);
                };
                clearTimeout(timeout);
                timeout = setTimeout(later, wait);
            };
        },

        // Throttle function
        throttle(func, limit) {
            let inThrottle;
            return function() {
                const args = arguments;
                const context = this;
                if (!inThrottle) {
                    func.apply(context, args);
                    inThrottle = true;
                    setTimeout(() => inThrottle = false, limit);
                }
            };
        },

        // Format number with commas
        formatNumber(num) {
            return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },

        // Generate unique ID
        generateId() {
            return Date.now().toString(36) + Math.random().toString(36).substr(2);
        }
    }
};

// Initialize when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    StudentApp.init();
});

// Add CSS for notifications and other dynamic elements
const dynamicCSS = `
    .notification-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 10000;
        max-width: 300px;
    }

    .notification {
        background: white;
        border-radius: 10px;
        padding: 15px;
        margin-bottom: 10px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.2);
        transform: translateX(100%);
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .notification.show {
        transform: translateX(0);
    }

    .notification-info { border-left: 4px solid #667eea; }
    .notification-success { border-left: 4px solid #28a745; }
    .notification-warning { border-left: 4px solid #ffc107; }
    .notification-error { border-left: 4px solid #dc3545; }

    .notification-close {
        background: none;
        border: none;
        font-size: 1.2rem;
        cursor: pointer;
        margin-left: auto;
        opacity: 0.7;
    }

    .notification-close:hover {
        opacity: 1;
    }

    .ripple {
        position: absolute;
        border-radius: 50%;
        background: rgba(255,255,255,0.3);
        transform: scale(0);
        animation: ripple-animation 0.6s ease-out;
        pointer-events: none;
    }

    @keyframes ripple-animation {
        to {
            transform: scale(2);
            opacity: 0;
        }
    }

    .loading-overlay {
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(255,255,255,0.9);
        display: flex;
        justify-content: center;
        align-items: center;
        z-index: 9999;
        backdrop-filter: blur(5px);
    }

    .sortable {
        position: relative;
        user-select: none;
    }

    .sortable::after {
        content: '⇅';
        position: absolute;
        right: 8px;
        opacity: 0.5;
        font-size: 0.8rem;
    }

    .sortable.sort-asc::after {
        content: '↑';
        opacity: 1;
        color: var(--primary-color);
    }

    .sortable.sort-desc::after {
        content: '↓';
        opacity: 1;
        color: var(--primary-color);
    }

    .animate-in {
        animation: fadeInUp 0.6s ease-out;
    }

    @keyframes fadeInUp {
        from {
            opacity: 0;
            transform: translateY(30px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .is-invalid {
        border-color: #dc3545 !important;
        box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25) !important;
    }

    .is-valid {
        border-color: #28a745 !important;
        box-shadow: 0 0 0 0.2rem rgba(40, 167, 69, 0.25) !important;
    }

    .invalid-feedback {
        display: block;
        width: 100%;
        margin-top: 0.25rem;
        font-size: 0.875rem;
        color: #dc3545;
    }

    tr.selected {
        background: linear-gradient(90deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1)) !important;
        border-left: 4px solid var(--primary-color);
    }
`;

// Inject dynamic CSS
const styleSheet = document.createElement('style');
styleSheet.textContent = dynamicCSS;
document.head.appendChild(styleSheet);

// Export for global access
window.StudentApp = StudentApp; 