/**
 * Utility Functions for Student Management System
 */

const Utils = {
    // Form validation utilities
    validation: {
        // Validate required fields
        validateRequired(element) {
            const value = element.value.trim();
            return value.length > 0;
        },

        // Validate student marks (0-100)
        validateMarks(marks) {
            const numValue = parseFloat(marks);
            return !isNaN(numValue) && numValue >= 0 && numValue <= 100;
        },

        // Validate student name (letters and spaces only)
        validateName(name) {
            const nameRegex = /^[a-zA-Z\s]+$/;
            return nameRegex.test(name.trim()) && name.trim().length >= 2;
        },

        // Validate form
        validateForm(form) {
            const errors = [];
            const inputs = form.querySelectorAll('input[required], select[required]');
            
            inputs.forEach(input => {
                const value = input.value.trim();
                const fieldName = input.getAttribute('placeholder') || input.name;
                
                if (!this.validateRequired(input)) {
                    errors.push(`${fieldName} is required`);
                    return;
                }

                // Specific validations
                if (input.name === 'firstName' || input.name === 'lastName') {
                    if (!this.validateName(value)) {
                        errors.push(`${fieldName} must contain only letters and spaces`);
                    }
                }

                if (input.name === 'marks') {
                    if (!this.validateMarks(value)) {
                        errors.push('Marks must be between 0 and 100');
                    }
                }
            });

            return {
                isValid: errors.length === 0,
                errors: errors
            };
        }
    },

    // Data formatting utilities
    formatting: {
        // Format marks with appropriate styling
        formatMarks(marks) {
            const numMarks = parseFloat(marks);
            let className = '';
            
            if (numMarks >= 90) className = 'text-success';
            else if (numMarks >= 75) className = 'text-info';
            else if (numMarks >= 60) className = 'text-warning';
            else className = 'text-danger';
            
            return `<span class="badge ${className}">${numMarks}%</span>`;
        },

        // Format student name
        formatName(firstName, lastName) {
            return `${firstName} ${lastName}`.replace(/\s+/g, ' ').trim();
        },

        // Format date
        formatDate(date) {
            return new Date(date).toLocaleDateString('vi-VN', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit'
            });
        },

        // Format number with commas
        formatNumber(num) {
            return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    },

    // DOM manipulation utilities
    dom: {
        // Add loading state to button
        setButtonLoading(button, loading = true) {
            if (loading) {
                button.dataset.originalText = button.innerHTML;
                button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Loading...';
                button.disabled = true;
            } else {
                button.innerHTML = button.dataset.originalText || button.innerHTML;
                button.disabled = false;
                delete button.dataset.originalText;
            }
        },

        // Show/hide element with animation
        toggleElement(element, show = true) {
            if (show) {
                element.style.display = 'block';
                element.style.opacity = '0';
                element.style.transform = 'translateY(-10px)';
                
                setTimeout(() => {
                    element.style.transition = 'all 0.3s ease';
                    element.style.opacity = '1';
                    element.style.transform = 'translateY(0)';
                }, 10);
            } else {
                element.style.transition = 'all 0.3s ease';
                element.style.opacity = '0';
                element.style.transform = 'translateY(-10px)';
                
                setTimeout(() => {
                    element.style.display = 'none';
                }, 300);
            }
        },

        // Add ripple effect
        addRipple(element, event) {
            const ripple = document.createElement('span');
            const rect = element.getBoundingClientRect();
            const size = Math.max(rect.width, rect.height);
            const x = event.clientX - rect.left - size / 2;
            const y = event.clientY - rect.top - size / 2;

            ripple.style.width = ripple.style.height = size + 'px';
            ripple.style.left = x + 'px';
            ripple.style.top = y + 'px';
            ripple.classList.add('ripple-effect');

            element.appendChild(ripple);

            setTimeout(() => {
                ripple.remove();
            }, 600);
        },

        // Smooth scroll to element
        scrollTo(element, offset = 0) {
            const elementPosition = element.offsetTop - offset;
            window.scrollTo({
                top: elementPosition,
                behavior: 'smooth'
            });
        }
    },

    // Local storage utilities
    storage: {
        // Save data to localStorage
        save(key, data) {
            try {
                localStorage.setItem(key, JSON.stringify(data));
                return true;
            } catch (e) {
                console.error('Failed to save to localStorage:', e);
                return false;
            }
        },

        // Load data from localStorage
        load(key, defaultValue = null) {
            try {
                const data = localStorage.getItem(key);
                return data ? JSON.parse(data) : defaultValue;
            } catch (e) {
                console.error('Failed to load from localStorage:', e);
                return defaultValue;
            }
        },

        // Remove data from localStorage
        remove(key) {
            try {
                localStorage.removeItem(key);
                return true;
            } catch (e) {
                console.error('Failed to remove from localStorage:', e);
                return false;
            }
        },

        // Clear all localStorage
        clear() {
            try {
                localStorage.clear();
                return true;
            } catch (e) {
                console.error('Failed to clear localStorage:', e);
                return false;
            }
        }
    },

    // Notification utilities
    notifications: {
        // Show success notification
        success(message, duration = 3000) {
            this.show(message, 'success', duration);
        },

        // Show error notification
        error(message, duration = 5000) {
            this.show(message, 'error', duration);
        },

        // Show warning notification
        warning(message, duration = 4000) {
            this.show(message, 'warning', duration);
        },

        // Show info notification
        info(message, duration = 3000) {
            this.show(message, 'info', duration);
        },

        // Show notification
        show(message, type = 'info', duration = 3000) {
            // Remove existing notifications
            document.querySelectorAll('.notification').forEach(n => n.remove());

            const notification = document.createElement('div');
            notification.className = `notification notification-${type}`;
            notification.innerHTML = `
                <div class="notification-content">
                    <i class="fas fa-${this.getIcon(type)}"></i>
                    <span>${message}</span>
                    <button class="notification-close" onclick="this.parentElement.parentElement.remove()">×</button>
                </div>
            `;

            // Add to body
            document.body.appendChild(notification);

            // Show with animation
            setTimeout(() => {
                notification.classList.add('show');
            }, 10);

            // Auto-hide
            setTimeout(() => {
                notification.classList.remove('show');
                setTimeout(() => {
                    notification.remove();
                }, 300);
            }, duration);
        },

        // Get icon for notification type
        getIcon(type) {
            const icons = {
                success: 'check-circle',
                error: 'exclamation-circle',
                warning: 'exclamation-triangle',
                info: 'info-circle'
            };
            return icons[type] || icons.info;
        }
    },

    // Table utilities
    table: {
        // Update table row
        updateRow(table, rowData, rowIndex) {
            const row = table.rows[rowIndex];
            if (!row) return false;

            Object.keys(rowData).forEach((key, index) => {
                if (row.cells[index]) {
                    row.cells[index].textContent = rowData[key];
                }
            });

            return true;
        },

        // Add new table row
        addRow(table, rowData) {
            const tbody = table.querySelector('tbody');
            const row = tbody.insertRow();
            
            rowData.forEach(cellData => {
                const cell = row.insertCell();
                cell.innerHTML = cellData;
            });

            return row;
        },

        // Remove table row
        removeRow(table, rowIndex) {
            const row = table.rows[rowIndex];
            if (row) {
                row.remove();
                return true;
            }
            return false;
        },

        // Clear table body
        clearTable(table) {
            const tbody = table.querySelector('tbody');
            if (tbody) {
                tbody.innerHTML = '';
            }
        },

        // Sort table by column
        sortTable(table, columnIndex, ascending = true) {
            const tbody = table.querySelector('tbody');
            const rows = Array.from(tbody.rows);
            
            rows.sort((a, b) => {
                const aValue = a.cells[columnIndex].textContent.trim();
                const bValue = b.cells[columnIndex].textContent.trim();
                
                // Check if values are numeric
                const aNum = parseFloat(aValue);
                const bNum = parseFloat(bValue);
                
                if (!isNaN(aNum) && !isNaN(bNum)) {
                    return ascending ? aNum - bNum : bNum - aNum;
                } else {
                    return ascending ? 
                        aValue.localeCompare(bValue) : 
                        bValue.localeCompare(aValue);
                }
            });

            // Re-append sorted rows
            rows.forEach(row => tbody.appendChild(row));
        }
    },

    // Ajax utilities
    ajax: {
        // Make GET request
        get(url, callback, errorCallback) {
            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => callback(data))
                .catch(error => {
                    console.error('GET request failed:', error);
                    if (errorCallback) errorCallback(error);
                });
        },

        // Make POST request
        post(url, data, callback, errorCallback) {
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => callback(data))
            .catch(error => {
                console.error('POST request failed:', error);
                if (errorCallback) errorCallback(error);
            });
        },

        // Make form POST request
        postForm(url, formData, callback, errorCallback) {
            fetch(url, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text(); // Spring MVC often returns HTML
            })
            .then(data => callback(data))
            .catch(error => {
                console.error('Form POST failed:', error);
                if (errorCallback) errorCallback(error);
            });
        }
    },

    // Math utilities
    math: {
        // Calculate average
        average(numbers) {
            if (numbers.length === 0) return 0;
            const sum = numbers.reduce((a, b) => a + b, 0);
            return sum / numbers.length;
        },

        // Calculate percentage
        percentage(part, total) {
            if (total === 0) return 0;
            return (part / total) * 100;
        },

        // Round to decimal places
        round(number, decimals = 2) {
            return Math.round(number * Math.pow(10, decimals)) / Math.pow(10, decimals);
        },

        // Clamp number between min and max
        clamp(number, min, max) {
            return Math.min(Math.max(number, min), max);
        }
    },

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

    // Generate unique ID
    generateId() {
        return Date.now().toString(36) + Math.random().toString(36).substr(2);
    },

    // Deep clone object
    deepClone(obj) {
        return JSON.parse(JSON.stringify(obj));
    },

    // Check if element is in viewport
    isInViewport(element) {
        const rect = element.getBoundingClientRect();
        return (
            rect.top >= 0 &&
            rect.left >= 0 &&
            rect.bottom <= (window.innerHeight || document.documentElement.clientHeight) &&
            rect.right <= (window.innerWidth || document.documentElement.clientWidth)
        );
    }
};

// Export for global access
window.Utils = Utils; 