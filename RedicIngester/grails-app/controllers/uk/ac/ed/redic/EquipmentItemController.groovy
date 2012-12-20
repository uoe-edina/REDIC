package uk.ac.ed.redic

import org.springframework.dao.DataIntegrityViolationException

class EquipmentItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
		
		if (params.sort == null || params.sort.length() == 0){
			params.sort="id"
			params.order="asc"
		}
		
        [equipmentItemInstanceList: EquipmentItem.list(params), equipmentItemInstanceTotal: EquipmentItem.count()]
    }

    def create() {
        [equipmentItemInstance: new EquipmentItem(params)]
    }

    def save() {
        def equipmentItemInstance = new EquipmentItem(params)
        if (!equipmentItemInstance.save(flush: true)) {
            render(view: "create", model: [equipmentItemInstance: equipmentItemInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), equipmentItemInstance.id])
        redirect(action: "show", id: equipmentItemInstance.id)
    }

    def show() {
        def equipmentItemInstance = EquipmentItem.get(params.id)
        if (!equipmentItemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), params.id])
            redirect(action: "list")
            return
        }

        [equipmentItemInstance: equipmentItemInstance]
    }

    def edit() {
        def equipmentItemInstance = EquipmentItem.get(params.id)
        if (!equipmentItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), params.id])
            redirect(action: "list")
            return
        }

        [equipmentItemInstance: equipmentItemInstance]
    }

    def update() {
        def equipmentItemInstance = EquipmentItem.get(params.id)
        if (!equipmentItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (equipmentItemInstance.version > version) {
                equipmentItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'equipmentItem.label', default: 'EquipmentItem')] as Object[],
                          "Another user has updated this EquipmentItem while you were editing")
                render(view: "edit", model: [equipmentItemInstance: equipmentItemInstance])
                return
            }
        }

        equipmentItemInstance.properties = params

        if (!equipmentItemInstance.save(flush: true)) {
            render(view: "edit", model: [equipmentItemInstance: equipmentItemInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), equipmentItemInstance.id])
        redirect(action: "show", id: equipmentItemInstance.id)
    }

    def delete() {
        def equipmentItemInstance = EquipmentItem.get(params.id)
        if (!equipmentItemInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), params.id])
            redirect(action: "list")
            return
        }

        try {
            equipmentItemInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'equipmentItem.label', default: 'EquipmentItem'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
