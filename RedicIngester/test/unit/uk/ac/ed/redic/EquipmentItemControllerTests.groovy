package uk.ac.ed.redic



import org.junit.*
import grails.test.mixin.*

@TestFor(EquipmentItemController)
@Mock(EquipmentItem)
class EquipmentItemControllerTests {


    def populateValidParams(params) {
      assert params != null
      // TODO: Populate valid properties like...
      //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/equipmentItem/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.equipmentItemInstanceList.size() == 0
        assert model.equipmentItemInstanceTotal == 0
    }

    void testCreate() {
       def model = controller.create()

       assert model.equipmentItemInstance != null
    }

    void testSave() {
        controller.save()

        assert model.equipmentItemInstance != null
        assert view == '/equipmentItem/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/equipmentItem/show/1'
        assert controller.flash.message != null
        assert EquipmentItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/equipmentItem/list'


        populateValidParams(params)
        def equipmentItem = new EquipmentItem(params)

        assert equipmentItem.save() != null

        params.id = equipmentItem.id

        def model = controller.show()

        assert model.equipmentItemInstance == equipmentItem
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/equipmentItem/list'


        populateValidParams(params)
        def equipmentItem = new EquipmentItem(params)

        assert equipmentItem.save() != null

        params.id = equipmentItem.id

        def model = controller.edit()

        assert model.equipmentItemInstance == equipmentItem
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/equipmentItem/list'

        response.reset()


        populateValidParams(params)
        def equipmentItem = new EquipmentItem(params)

        assert equipmentItem.save() != null

        // test invalid parameters in update
        params.id = equipmentItem.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/equipmentItem/edit"
        assert model.equipmentItemInstance != null

        equipmentItem.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/equipmentItem/show/$equipmentItem.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        equipmentItem.clearErrors()

        populateValidParams(params)
        params.id = equipmentItem.id
        params.version = -1
        controller.update()

        assert view == "/equipmentItem/edit"
        assert model.equipmentItemInstance != null
        assert model.equipmentItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/equipmentItem/list'

        response.reset()

        populateValidParams(params)
        def equipmentItem = new EquipmentItem(params)

        assert equipmentItem.save() != null
        assert EquipmentItem.count() == 1

        params.id = equipmentItem.id

        controller.delete()

        assert EquipmentItem.count() == 0
        assert EquipmentItem.get(equipmentItem.id) == null
        assert response.redirectedUrl == '/equipmentItem/list'
    }
}
